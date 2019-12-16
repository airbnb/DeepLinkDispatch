package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex

import java.util.Collections

/**
 * BaseLoader classes are used for keeping a registry of deep links and dispatching them
 * from Intents.
 *
 * This uses a List of DeepLinkEntries collected from a Module. And a binary match index.
 *
 * Both of them are initialzied by annotation processor generated children of this class.
 */
abstract class BaseLoader(val registeredDeepLinks: List<DeepLinkEntry>,
                          /**
                           * A binary match index, created by the annotation processor.
                           * In a wrapper to make handling it easier
                           */
                          matchIndexArray: ByteArray) {

    private val matchIndex: MatchIndex

    init {
        this.matchIndex = MatchIndex(matchIndexArray)
    }

    /**
     * Use a binary match index to match the given URL. Defaults can be find in [MatchIndex].
     *
     * @param deepLinkUri The input [DeepLinkUri] to match
     * @return A [DeepLinkEntry] if a match was found or null if not.
     */
    fun idxMatch(deepLinkUri: DeepLinkUri?): DeepLinkEntry? {
        if (deepLinkUri == null) {
            return null;
        }
        // Generating a match list (list of elements in to be matched URL starting with an
        // (artificial) root.
        val match = matchIndex.matchUri(SchemeHostAndPath(deepLinkUri).matchList,
                null, 0, 0, matchIndex.length())
        if (match != null) {
            val matchedEntry = registeredDeepLinks[match.matchIndex]
            matchedEntry.setParameters(deepLinkUri, match.parameterMap)
            return matchedEntry
        }
        return null
    }
}
