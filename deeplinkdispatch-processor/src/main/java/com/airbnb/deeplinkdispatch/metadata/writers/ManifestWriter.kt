package com.airbnb.deeplinkdispatch.metadata.writers

import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.CONFIGURABLE_PATH_SEGMENT_PREFIX
import com.airbnb.deeplinkdispatch.CONFIGURABLE_PATH_SEGMENT_SUFFIX
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import com.airbnb.deeplinkdispatch.SIMPLE_GLOB_PATTERN_MIN_ONE_CHAR
import com.airbnb.deeplinkdispatch.allPossibleValues
import java.io.PrintWriter

/**
 * Writes Android manifest intent-filter entries for deep links.
 *
 * The grouping algorithm ensures:
 * 1. Minimum number of intent-filters (for manifest efficiency)
 * 2. No invalid URL matches (correctness)
 *
 * URLs can only be merged into one intent-filter if their combined tuple sets form a
 * complete Cartesian product (https://en.wikipedia.org/wiki/Cartesian_product).
 * This is because Android's intent-filter matching uses OR logic:
 * scheme matches ANY listed scheme, host matches ANY listed host, path matches ANY listed path.
 *
 * For example, merging deeplink://app/section1 and deeplink://nav/page would incorrectly
 * match deeplink://app/page (not defined!).
 */
internal class ManifestWriter : Writer {
    override fun write(
        env: XProcessingEnv,
        writer: PrintWriter,
        elements: List<DeepLinkAnnotatedElement>,
    ) {
        writer.apply {
            println("<?xml version=\"1.0\" encoding=\"utf-8\"?>")
            println("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\" >")
            println("    <application>")
            elements
                .filter { it.activityClassFqn != null }
                .groupBy { it.activityClassFqn }
                .forEach { activityClassFqn, activityElements ->
                    println("        <activity")
                    println("            android:name=\"$activityClassFqn\" android:exported=\"true\">")

                    // First group by intent-filter attributes (actions, categories, intentFilterAttributes)
                    // These must always match exactly for URLs to share an intent-filter
                    activityElements
                        .groupBy { element ->
                            IntentFilterAttributes(
                                actions = element.actions,
                                categories = element.categories,
                                intentFilterAttributes = element.intentFilterAttributes,
                            )
                        }.forEach { (filterAttributes, elementsWithSameAttributes) ->
                            // Within each attribute group, find mergeable URL groups using Cartesian product rule
                            val mergeableGroups = findMergeableGroups(elementsWithSameAttributes)

                            mergeableGroups.forEach { group ->
                                writeIntentFilter(filterAttributes, group)
                            }
                        }

                    println("        </activity>")
                }
            println("    </application>")
            println("</manifest>")
            flush()
        }
    }

    /**
     * Finds groups of URLs that can be safely merged into single intent-filters.
     *
     * Uses the Cartesian product rule: URLs can be merged if their combined tuple sets
     * exactly equal the cross product of all schemes × all hosts × all paths.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Cartesian_product">Cartesian product</a>
     */
    internal fun findMergeableGroups(elements: List<DeepLinkAnnotatedElement>): List<List<DeepLinkAnnotatedElement>> {
        if (elements.isEmpty()) return emptyList()

        // Expand each element to its tuple set
        val expanded =
            elements.map { element ->
                ExpandedUrl(
                    schemes =
                        element.deepLinkUri
                            .scheme()
                            .allPossibleValues()
                            .toSet(),
                    hosts =
                        element.deepLinkUri
                            .host()
                            .allPossibleValues()
                            .toSet(),
                    paths =
                        element.deepLinkUri
                            .pathSegments()
                            .joinToString(prefix = "/", separator = "/")
                            .allPossibleValues()
                            .toSet(),
                    element = element,
                )
            }

        // Greedy algorithm: try to add each URL to an existing group, or create new group
        val groups = mutableListOf<MutableList<ExpandedUrl>>()

        for (url in expanded) {
            var merged = false
            for (group in groups) {
                if (canMerge(group + url)) {
                    group.add(url)
                    merged = true
                    break
                }
            }
            if (!merged) {
                groups.add(mutableListOf(url))
            }
        }

        // Try to merge groups with each other (handles cases where order matters)
        var changed = true
        while (changed) {
            changed = false
            outer@ for (i in groups.indices) {
                for (j in i + 1 until groups.size) {
                    if (canMerge(groups[i] + groups[j])) {
                        groups[i].addAll(groups[j])
                        groups.removeAt(j)
                        changed = true
                        break@outer
                    }
                }
            }
        }

        return groups.map { group -> group.map { it.element } }
    }

    /**
     * Checks if a group of URLs can be merged into a single intent-filter.
     *
     * The condition is: the combined tuple set must exactly equal the Cartesian product
     * of all schemes × all hosts × all paths. If not, merging would create invalid matches.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Cartesian_product">Cartesian product</a>
     */
    internal fun canMerge(urls: List<ExpandedUrl>): Boolean {
        val allSchemes = urls.flatMap { it.schemes }.toSet()
        val allHosts = urls.flatMap { it.hosts }.toSet()
        val allPaths = urls.flatMap { it.paths }.toSet()

        // Expected: complete Cartesian product
        val expectedSize = allSchemes.size * allHosts.size * allPaths.size

        // Actual: union of all URL tuples
        val actualTuples =
            urls
                .flatMap { url ->
                    url.schemes.flatMap { s ->
                        url.hosts.flatMap { h ->
                            url.paths.map { p -> Triple(s, h, p) }
                        }
                    }
                }.toSet()

        // Can merge only if actual equals expected (complete Cartesian product)
        return actualTuples.size == expectedSize
    }

    private fun PrintWriter.writeIntentFilter(
        filterAttributes: IntentFilterAttributes,
        elements: List<DeepLinkAnnotatedElement>,
    ) {
        val attributesString =
            if (filterAttributes.intentFilterAttributes.isNotEmpty()) {
                filterAttributes.intentFilterAttributes.joinToString(separator = " ", prefix = " ")
            } else {
                ""
            }

        println("            <intent-filter$attributesString>")

        filterAttributes.actions.forEach { action ->
            println("                <action android:name=\"$action\" />")
        }
        filterAttributes.categories.forEach { category ->
            println("                <category android:name=\"$category\" />")
        }

        // Collect all unique scheme, host, and path values from all elements in this group
        val allPossibleUrlValues =
            elements
                .map { element ->
                    val scheme = element.deepLinkUri.scheme()
                    val host = element.deepLinkUri.host()
                    val path =
                        element.deepLinkUri
                            .pathSegments()
                            .joinToString(prefix = "/", separator = "/")
                    UrlValues(
                        scheme.allPossibleValues().toSet(),
                        host.allPossibleValues().toSet(),
                        // If the path is just "" we support "" and "/".
                        path
                            .allPossibleValues()
                            .toSet()
                            .let { if (it == setOf("/")) setOf("/", "") else it },
                    )
                }.reduce { acc, urlValues ->
                    UrlValues(
                        acc.schemeValues + urlValues.schemeValues,
                        acc.hostValues + urlValues.hostValues,
                        acc.pathValues + urlValues.pathValues,
                    )
                }

        allPossibleUrlValues.schemeValues.forEach { schemeValue ->
            println("                <data android:scheme=\"$schemeValue\" />")
        }
        allPossibleUrlValues.hostValues.forEach { hostValue ->
            println("                <data android:host=\"$hostValue\" />")
        }
        allPossibleUrlValues.pathValues
            .map { pathValue ->
                // The "/<name>" in the url template becomes ${name} to be replaced as a manifest placeholder
                // Note that we do replace / before the <name> also as we do allow to replace the whole path segment
                // with nothing and in that case also need to remove the / this means that ${name} will need to contain
                // the / it not empty.
                configurablePathSegmentRegex.replace(pathValue, "\\$\\{$1\\}")
            }.forEach { pathValue ->
                // If there is a simple glob pattern in the path, we need to use pathPattern instead of path
                // See: https://developer.android.com/guide/topics/manifest/data-element#path
                println(
                    "                <data android:${
                        if (pathValue.contains(SIMPLE_GLOB_PATTERN_MIN_ONE_CHAR)) {
                            "pathPattern"
                        } else {
                            "path"
                        }
                    }=\"$pathValue\" />",
                )
            }
        println("            </intent-filter>")
    }

    companion object {
        private val configurablePathSegmentRegex =
            "\\/?$CONFIGURABLE_PATH_SEGMENT_PREFIX([^$CONFIGURABLE_PATH_SEGMENT_SUFFIX]*)$CONFIGURABLE_PATH_SEGMENT_SUFFIX"
                .toRegex()
    }
}

/**
 * Attributes that must match exactly for URLs to share an intent-filter.
 */
private data class IntentFilterAttributes(
    val actions: Set<String>,
    val categories: Set<String>,
    val intentFilterAttributes: Set<String>,
)

/**
 * A URL expanded to all its possible (scheme, host, path) combinations.
 */
internal data class ExpandedUrl(
    val schemes: Set<String>,
    val hosts: Set<String>,
    val paths: Set<String>,
    val element: DeepLinkAnnotatedElement,
)

private data class UrlValues(
    val schemeValues: Set<String>,
    val hostValues: Set<String>,
    val pathValues: Set<String>,
)
