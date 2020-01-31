package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex

/**
 * BaseRegistry classes are used for keeping a registry of deep links and dispatching them
 * from Intents.
 *
 * This uses a List of DeepLinkEntries collected from a Module. And a binary match index.
 *
 * Both of them are initialized by annotation processor generated children of this class.
 */
abstract class BaseRegistry(val registeredDeepLinks: List<DeepLinkEntry>,
                            /**
                             * A binary match index, created by the annotation processor.
                             * In a wrapper to make handling it easier
                             */
                            matchIndexArray: ByteArray,
                            val pathSegmentKeysInRegistry: Set<String>) {

    private val matchIndex: MatchIndex = MatchIndex(matchIndexArray)

    /**
     * Use a binary match index to match the given URL. Defaults can be find in [MatchIndex].
     *
     * @param deepLinkUri The input [DeepLinkUri] to match
     * @return A [DeepLinkEntry] if a match was found or null if not.
     */
    @JvmOverloads
    fun idxMatch(deepLinkUri: DeepLinkUri?, pathSegmentReplacements: Map<String, String> = mapOf()): DeepLinkEntry? {
        if (deepLinkUri == null) {
            return null
        }
        // Generating a match list (list of elements in to be matched URL starting with an
        // (artificial) root.
        val match = matchIndex.matchUri(SchemeHostAndPath(deepLinkUri).matchList,
                null, 0, 0, matchIndex.length(),
                pathSegmentReplacements)
        if (match != null) {
            val matchedEntry = registeredDeepLinks[match.matchIndex]
            matchedEntry.setParameters(deepLinkUri, match.parameterMap)
            return matchedEntry
        }
        return null
    }
}
