package com.airbnb.deeplinkdispatch.base

import com.airbnb.deeplinkdispatch.configurablePathSegmentSuffix
import com.airbnb.deeplinkdispatch.configurablePathSegmentPrefix
import java.io.InputStream

object Utils {

    @JvmStatic
    fun readMatchIndexFromStrings(strings: Array<String>): ByteArray? {
        if (strings.isEmpty()) {
            return byteArrayOf()
        }
        if (strings.size == 1) {
            return strings[0].toByteArray(charset(MatchIndex.MATCH_INDEX_ENCODING))
        }
        val fullString = StringBuilder(strings.sumBy { it.length })
        strings.forEach { fullString.append(it) }
        return fullString.toString().toByteArray(charset(MatchIndex.MATCH_INDEX_ENCODING))
    }

    private fun getBytes(inputStream: InputStream): ByteArray {
        return inputStream.readBytes()
    }

    @JvmStatic
    fun isConfigurablePathSegment(pathSegment: String) =
            pathSegment.startsWith(configurablePathSegmentPrefix)
                    && pathSegment.endsWith(configurablePathSegmentSuffix)
}
