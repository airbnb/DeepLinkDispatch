package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import com.airbnb.deeplinkdispatch.base.Utils

/**
 * BaseRegistry classes are used for keeping a registry of deep links and dispatching them
 * from Intents.
 *
 * This uses a List of DeepLinkEntries collected from a Module. And a binary match index.
 *
 * Both of them are initialized by annotation processor generated children of this class.
 * @param matchIndexArray ByteArray encoding of a tree of different UriComponents like scheme, host,
 * path segment. See [TreeNode].
 * @param pathSegmentReplacementKeysInRegistry Each registry's export of the path segments that are
 * declared in the module's deep links. A corresponding key-value pair must be passed into
 * [BaseDeepLinkDelegate]. The correspondent's presence will be validated at runtime by
 * [com.airbnb.deeplinkdispatch.ValidationUtilsKt.validateConfigurablePathSegmentReplacements].
 */
abstract class BaseRegistry(matchIndexArray: ByteArray,
                            pathSegmentReplacementKeys: Array<String>) {
    val pathSegmentReplacementKeysInRegistry = Utils.toByteArraysList(pathSegmentReplacementKeys)
    private val matchIndex: MatchIndex = MatchIndex(matchIndexArray)

    /**
     * Use a binary match index to match the given URL. Defaults can be find in [MatchIndex].
     *
     * @param deepLinkUri The inbound [DeepLinkUri] to match. In the typical use-case, this inbound
     * URI comes from an Android Intent.
     * @param pathSegmentReplacements originally passed into DLD via [BaseDeepLinkDelegate]. User
     * provided replacement values for configurable path segments.
     * Ex: mapOf("replaceable" to "swish") and
     * a://host/<replaceable> will result in a://host/swish being a match (and
     * a://host/<replaceable> is not a match).
     * @return A [DeepLinkEntry] if a match was found or null if not.
     */
    @JvmOverloads
    fun idxMatch(deepLinkUri: DeepLinkUri?, pathSegmentReplacements: Map<ByteArray, ByteArray> = mapOf()): DeepLinkMatchResult? {
        if (deepLinkUri == null) {
            return null
        }
        // Generating a match list (list of elements in to be matched URL starting with an
        // (artificial) root.
        return matchIndex.matchUri(
            deepLinkUri,
            SchemeHostAndPath(deepLinkUri).matchList,
            emptyMap(),
            0,
            0,
            matchIndex.length(),
            pathSegmentReplacements,
        )
    }

    /**
     * Get all DeepLinkEntries that can be matched by this registry
     */
    fun getAllEntries(): List<DeepLinkEntry> {
        return matchIndex.getAllEntries(0, matchIndex.length());
    }

    fun supports(
        deepLinkUri: DeepLinkUri?,
        pathSegmentReplacements: Map<ByteArray, ByteArray> = mapOf()
    ) = idxMatch(deepLinkUri, pathSegmentReplacements) != null
}
