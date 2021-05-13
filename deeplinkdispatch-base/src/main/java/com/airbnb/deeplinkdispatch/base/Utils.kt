package com.airbnb.deeplinkdispatch.base

import com.airbnb.deeplinkdispatch.componentParamPrefix
import com.airbnb.deeplinkdispatch.componentParamSuffix
import com.airbnb.deeplinkdispatch.configurablePathSegmentSuffix
import com.airbnb.deeplinkdispatch.configurablePathSegmentPrefix
import java.io.InputStream

/**
 * Chunk a CharSequence based on how long it's Modified UTF-8
 * (https://en.wikipedia.org/wiki/UTF-8#Modified_UTF-8) ByteArray representation would be.
 */
fun CharSequence.chunkOnModifiedUtf8ByteSize(chunkSize: Int): List<CharSequence> {
    require(chunkSize >= 3) {
        "UTF-8 chars can be up to 3 bytes wide. Minumum chunk size is 3 bytes."
    }
    val result = mutableListOf<CharSequence>()
    var nextChunkStart = 0
    for (i in 0 until this.length) {
        // Get the byte array for current chunk and check how many bytes this would take up.
        // U+0000 is encoded as two bytes C080 in modified UTF-8, which is used by Java to
        // store strings in the string table in class files.
        val charModifiedUtf8ByteArraySize = this.substring(nextChunkStart, i + 1)
                .let { chunk -> chunk.toByteArray().size + chunk.count { char -> char == '\u0000' } }

        // See if this char would still fit into the chunk. If not, create chunk and start next one.
        if (charModifiedUtf8ByteArraySize > chunkSize) {
            result.add(this.subSequence(nextChunkStart, i))
            nextChunkStart = i
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
    fun toByteArrayMap(input: Map<String, String>): Map<ByteArray, ByteArray> {
        return input.entries.associate { it.key.toByteArray() to it.value.toByteArray() }
    }

    @JvmStatic
    fun toByteArraysList(input: Array<String>): List<ByteArray> {
        return input.map { it.toByteArray() }.toList()
    }

    private fun getBytes(inputStream: InputStream): ByteArray {
        return inputStream.readBytes()
    }
    /**
     * @return true if Component Param, false if not. Throws IllegalArgumentException if contains
     * [componentParamSuffix] or [componentParamPrefix], but out of order.
     */
    fun validateIfComponentParam(uriComponent: String): Boolean {
        val start = uriComponent.indexOf(componentParamPrefix)
        val end = uriComponent.indexOf(componentParamSuffix)
        // -1 is the "not found" result for String#indexOf()
        if (start != -1 || end != -1) {
            require(start < end) { "Invalid URI component: $uriComponent. $componentParamPrefix must come before $componentParamSuffix." }
            require(start != -1 && end != -1) {
                "Invalid URI component: $uriComponent. If either" +
                        "$componentParamPrefix or $componentParamSuffix is present, then they must both be present and $componentParamPrefix must occur before $componentParamSuffix."
            }
            return true
        }
        return false
    }

    /**
     * If a [pathSegment] contains either [configurablePathSegmentPrefix] or [configurablePathSegmentSuffix] then it must
     * start and end with them, respectively.
     * @return true if [pathSegment] looks like a valid configurable path segment, false if it is not a configurable path segment.
     */
    fun validateIfConfigurablePathSegment(pathSegment: String): Boolean {
        if (pathSegment.contains(Regex("$configurablePathSegmentPrefix|$configurablePathSegmentSuffix"))) {
            require(isConfigurablePathSegment(pathSegment)) {
                ("Malformed path segment: $pathSegment! If it contains $configurablePathSegmentPrefix or $configurablePathSegmentSuffix, it must start with $configurablePathSegmentPrefix and end with $configurablePathSegmentSuffix.")
            }
            return true
        }
        return false
    }

    @JvmStatic
    fun isConfigurablePathSegment(pathSegment: String) =
            pathSegment.startsWith(configurablePathSegmentPrefix)
                    && pathSegment.endsWith(configurablePathSegmentSuffix)
}
