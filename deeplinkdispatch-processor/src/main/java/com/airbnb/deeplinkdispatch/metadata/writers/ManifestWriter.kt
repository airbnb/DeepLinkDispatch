package com.airbnb.deeplinkdispatch.metadata.writers

import androidx.room.compiler.processing.XProcessingEnv
import com.airbnb.deeplinkdispatch.DeepLinkAnnotatedElement
import com.airbnb.deeplinkdispatch.SIMPLE_GLOB_PATTERN
import com.airbnb.deeplinkdispatch.allPossibleValues
import java.io.PrintWriter

/**
 * Old documentation format.
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
                .forEach { activityClassFqn, elements ->
                    // Different paths might onbly be valid for different schemes and hosts, so we need to
                    // group by schemes and hosts as well.
                    println("        <activity")
                    println("            android:name=\"$activityClassFqn\" android:exported=\"true\">")
                    elements
                        .groupBy { deepLinkAnnotatedElement ->
                            deepLinkAnnotatedElement.deepLinkUri.let { deepLinkUri ->
                                Pair(
                                    deepLinkUri.scheme(),
                                    deepLinkUri.host(),
                                )
                            }
                        }.forEach { (schemeHostPair, elements) ->
                            println("            <intent-filter>")
                            println("                <action android:name=\"android.intent.action.VIEW\" />")
                            println("                <category android:name=\"android.intent.category.DEFAULT\" />")
                            println("                <category android:name=\"android.intent.category.BROWSABLE\" />")
                            // There might be multiple URIs in a single intent filter, and we want to make sure there
                            // are no duplicates (e.g. http and https for host over and over again)
                            val allPossibleUrlValues =
                                elements
                                    .map { element ->
                                        val scheme = schemeHostPair.first
                                        val host = schemeHostPair.second
                                        val path = element.deepLinkUri.pathSegments().joinToString(prefix = "/", separator = "/")
                                        UrlValues(
                                            scheme.allPossibleValues().toSet(),
                                            host.allPossibleValues().toSet(),
                                            path.allPossibleValues().toSet(),
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
                            allPossibleUrlValues.pathValues.forEach { pathValue ->
                                // If there is a simple glob pattern in the path, we need to use pathPattern instead of path
                                // See: https://developer.android.com/guide/topics/manifest/data-element#path
                                println(
                                    "                <data android:${
                                        if (pathValue.contains(SIMPLE_GLOB_PATTERN)) {
                                            "pathPattern"
                                        } else {
                                            "path"
                                        }
                                    }=\"$pathValue\" />",
                                )
                            }
                            println("            </intent-filter>")
                        }
                    println("        </activity>")
                }
            println("    </application>")
            println("</manifest>")
            flush()
        }
    }
}

private data class UrlValues(
    val schemeValues: Set<String>,
    val hostValues: Set<String>,
    val pathValues: Set<String>,
)
