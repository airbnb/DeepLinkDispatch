package com.airbnb.deeplinkdispatch.base

import com.airbnb.deeplinkdispatch.COMPONENT_PARAM_PREFIX
import com.airbnb.deeplinkdispatch.COMPONENT_PARAM_SUFFIX
import com.airbnb.deeplinkdispatch.CONFIGURABLE_PATH_SEGMENT_PREFIX
import com.airbnb.deeplinkdispatch.CONFIGURABLE_PATH_SEGMENT_SUFFIX
import java.io.InputStream

/**
 * Calculate the Modified UTF-8 byte size for a single character.
 * In Modified UTF-8:
 * - U+0000 (null) is encoded as 2 bytes (C0 80)
 * - U+0001 to U+007F are encoded as 1 byte
 * - U+0080 to U+07FF are encoded as 2 bytes
 * - U+0800 to U+FFFF are encoded as 3 bytes
 */
private fun Char.modifiedUtf8ByteSize(): Int {
    val codePoint = this.code
    return when {
        codePoint == 0 -> 2  // Null character is encoded as 2 bytes in Modified UTF-8
        codePoint <= 0x7F -> 1
        codePoint <= 0x7FF -> 2
        else -> 3
    }
}

/**
 * Chunk a CharSequence based on how long it's Modified UTF-8
 * (https://en.wikipedia.org/wiki/UTF-8#Modified_UTF-8) ByteArray representation would be.
 *
 * This implementation uses O(n) time complexity by calculating byte sizes incrementally
 * instead of creating substrings for each character.
 */
fun CharSequence.chunkOnModifiedUtf8ByteSize(chunkSize: Int): List<CharSequence> {
    require(chunkSize >= 3) {
        "UTF-8 chars can be up to 3 bytes wide. Minumum chunk size is 3 bytes."
    }
    val result = mutableListOf<CharSequence>()
    var nextChunkStart = 0
    var currentChunkByteSize = 0

    for (i in 0 until this.length) {
        val charByteSize = this[i].modifiedUtf8ByteSize()

        // See if this char would still fit into the chunk. If not, create chunk and start next one.
        if (currentChunkByteSize + charByteSize > chunkSize) {
            result.add(this.subSequence(nextChunkStart, i))
            nextChunkStart = i
            currentChunkByteSize = charByteSize
        } else {
            currentChunkByteSize += charByteSize
        }
    }
    // If there was a chunk that we started but did not add yet, add the rest.
    if (nextChunkStart != length) {
        result.add(this.subSequence(nextChunkStart, this.length))
    }
    return result
}

object Utils {
    @JvmStatic
    fun readMatchIndexFromStrings(strings: Array<String>): ByteArray? {
        if (strings.isEmpty()) {
            return byteArrayOf()
        }
        if (strings.size == 1) {
            return strings[0].toByteArray(charset(MatchIndex.MATCH_INDEX_ENCODING))
        }
        val fullString = StringBuilder(strings.sumOf { it.length })
        strings.forEach { fullString.append(it) }
        return fullString.toString().toByteArray(charset(MatchIndex.MATCH_INDEX_ENCODING))
    }

    @JvmStatic
    fun toByteArrayMap(input: Map<String, String>): Map<ByteArray, ByteArray> =
        input.entries.associate { it.key.toByteArray() to it.value.toByteArray() }

    @JvmStatic
    fun toByteArraysList(input: Array<String>): List<ByteArray> = input.map { it.toByteArray() }.toList()

    private fun getBytes(inputStream: InputStream): ByteArray = inputStream.readBytes()

    /**
     * @return true if Component Param, false if not. Throws IllegalArgumentException if contains
     * [COMPONENT_PARAM_SUFFIX] or [COMPONENT_PARAM_PREFIX], but out of order.
     */
    fun validateIfComponentParam(uriComponent: String): Boolean {
        val start = uriComponent.indexOf(COMPONENT_PARAM_PREFIX)
        val end = uriComponent.indexOf(COMPONENT_PARAM_SUFFIX)
        // -1 is the "not found" result for String#indexOf()
        if (start != -1 || end != -1) {
            require(
                start < end,
            ) { "Invalid URI component: $uriComponent. $COMPONENT_PARAM_PREFIX must come before $COMPONENT_PARAM_SUFFIX." }
            require(start != -1 && end != -1) {
                "Invalid URI component: $uriComponent. If either" +
                    "$COMPONENT_PARAM_PREFIX or $COMPONENT_PARAM_SUFFIX is present, then they must both be present and $COMPONENT_PARAM_PREFIX must occur before $COMPONENT_PARAM_SUFFIX."
            }
            return true
        }
        return false
    }

    /**
     * If a [pathSegment] contains either [CONFIGURABLE_PATH_SEGMENT_PREFIX] or [CONFIGURABLE_PATH_SEGMENT_SUFFIX] then it must
     * start and end with them, respectively.
     * @return true if [pathSegment] looks like a valid configurable path segment, false if it is not a configurable path segment.
     */
    fun validateIfConfigurablePathSegment(pathSegment: String): Boolean {
        if (pathSegment.contains(Regex("$CONFIGURABLE_PATH_SEGMENT_PREFIX|$CONFIGURABLE_PATH_SEGMENT_SUFFIX"))) {
            require(isConfigurablePathSegment(pathSegment)) {
                (
                    "Malformed path segment: $pathSegment! If it contains $CONFIGURABLE_PATH_SEGMENT_PREFIX" +
                        " or $CONFIGURABLE_PATH_SEGMENT_SUFFIX, it must start with" +
                        " $CONFIGURABLE_PATH_SEGMENT_PREFIX and end with $CONFIGURABLE_PATH_SEGMENT_SUFFIX."
                )
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun isConfigurablePathSegment(pathSegment: String) =
        pathSegment.startsWith(CONFIGURABLE_PATH_SEGMENT_PREFIX) &&
            pathSegment.endsWith(CONFIGURABLE_PATH_SEGMENT_SUFFIX)
}
