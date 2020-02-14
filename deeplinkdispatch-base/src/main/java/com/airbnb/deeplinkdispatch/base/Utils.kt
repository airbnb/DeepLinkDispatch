@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.airbnb.deeplinkdispatch.base

import com.airbnb.deeplinkdispatch.TreeNode.NodeMetadata.*
import java.io.InputStream
import kotlin.experimental.and

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
    fun isComponentTypeRoot(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentTypeRoot.flag.toByte() == IsComponentTypeRoot.flag.toByte()

    @JvmStatic
    fun isComponentTypeScheme(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentTypeScheme.flag.toByte() == IsComponentTypeScheme.flag.toByte()

    @JvmStatic
    fun isComponentTypeHost(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentTypeHost.flag.toByte() == IsComponentTypeHost.flag.toByte()

    @JvmStatic
    fun isComponentTypePathSegment(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentTypePathSegment.flag.toByte() == IsComponentTypePathSegment.flag.toByte()

    /**
     * In DLD, any component of a URI can contain a placeholder that will be substituted.
     * The placeholder does not need to occupy the entire component.
     * <p>E.g.</p>
     * <ul>
     * <li>myScheme://app{Placeholder}Name/</li>
     * <li>Placeholder: One</li>
     * <li>result: myScheme://appOneName/</li>
     * </ul>
     * */
    @JvmStatic
    fun isComponentParam(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsComponentParam.flag.toByte() == IsComponentParam.flag.toByte()

    /**
     * A Configurable Path Segment represents a path segment which will lookup a dynamic (runtime)
     * provided replacement value when it is visited. The entire path segment will be replaced.
     */
    @JvmStatic
    fun isConfigurablePathSegment(nodeMetadata: Byte): Boolean =
            nodeMetadata and IsConfigurablePathSegment.flag.toByte() == IsConfigurablePathSegment.flag.toByte()
}
