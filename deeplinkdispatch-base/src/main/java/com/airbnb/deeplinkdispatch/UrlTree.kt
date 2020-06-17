@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS", "EXPERIMENTAL_OVERRIDE")

package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex.*
import com.airbnb.deeplinkdispatch.base.Utils.validateIfComponentParam
import com.airbnb.deeplinkdispatch.base.Utils.validateIfConfigurablePathSegment
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

data class UriMatch(val uri: DeepLinkUri, val matchId: Int, val annotatedElement: String, val annotatedMethod: String?)

@kotlin.ExperimentalUnsignedTypes
open class TreeNode(open val id: String, internal val metadata: NodeMetadata) {

    val children = mutableSetOf<TreeNode>()
    var match: UriMatch? = null
        set(value) {
            if (field != null) error("Ambiguous URI. Same match for two URIs ($field vs $value)") else field = value
        }

    fun addNode(node: TreeNode): TreeNode {
        return if (children.add(node)) node else children.first { it == node }
    }

    fun serializedId(): String {
        if (metadata.isConfigurablePathSegment) {
            return id.substringAfter(configurablePathSegmentPrefix).substringBefore(configurablePathSegmentSuffix)
        }
        return id
    }

    /**
     * Byte array format is:
     * 0                                                    [NodeMetadata] flags; 1 byte
     * 1                                                    length of value sub-array
     * 2...5                                                length of node's children sub-array
     * 6...7                                                match id
     * 8..(8+value length)                                  actual value sub-array
     * (8+value length)..((8+value length)+children length) actual children sub-array
     */
    fun toUByteArray(): UByteArray {
        val childrenByteArrays: List<UByteArray> = generateChildrenByteArrays()
        val valueByteArray = serializedId().toByteArray(UTF_8).toUByteArray()
        val header = generateHeader(metadata, valueByteArray, childrenByteArrays, match)
        val resultByteArray = UByteArray(arrayLength(
                childrenByteArrays,
                valueByteArray,
                header
        ))
        header.copyInto(resultByteArray)
        var position = header.size
        with(valueByteArray) {
            copyInto(resultByteArray, position)
            position += size
        }
        for (childByteArray in childrenByteArrays) {
            childByteArray.copyInto(resultByteArray, position)
            position += childByteArray.size
        }
        return resultByteArray
    }

    private fun arrayLength(childArrays: List<UByteArray>, value: UByteArray, header: UByteArray): Int {
        return header.size + value.size + childArrays.sumBy { it.size }
    }

    // Make sure we match concrete matches before placeholders or configurable path segments
    private fun generateChildrenByteArrays(): List<UByteArray> = children.sortedWith(compareBy({ it.metadata.isConfigurablePathSegment }, {it.metadata.isComponentParam}, {it.id})).map { it.toUByteArray() }

    private fun generateHeader(metadata: NodeMetadata, value: UByteArray, children: List<UByteArray>? = null, match: UriMatch?): UByteArray {
        val childrenLength: Int = children?.sumBy { it.size } ?: 0
        return UByteArray(HEADER_LENGTH).apply {
            set(0, metadata.metadata.toUByte())
            set(1, value.size.toUByte())
            writeUIntAt(2, childrenLength.toUInt())
            writeUShortAt(6, match?.matchId?.toUShort() ?: NO_MATCH.toUShort())
        }
    }
}


private const val MAX_EXPORT_STRING_SIZE = 50000

data class Root(override val id: String = "r") : TreeNode(ROOT_VALUE, NodeMetadata(MetadataMasks.ComponentTypeRootMask, id)) {
    fun writeToOutoutStream(openOutputStream: OutputStream) {
        openOutputStream.write(this.toUByteArray().toByteArray())
    }

    /**
     * Convert the byte array into a string return as max 60k length sset of strings.
     */
    fun getStrings(): List<String> {
        return String(bytes = this.toUByteArray().toByteArray(), charset = Charset.forName(MATCH_INDEX_ENCODING)).chunked(MAX_EXPORT_STRING_SIZE)
    }

    /**
     * Add the given DeepLinkUri to the the trie
     */
    fun addToTrie(matchIndex: Int, deepLinkUri: DeepLinkUri, annotatedElement: String, annotatedMethod: String?) {
        var node = this.addNode(Scheme(deepLinkUri.scheme().also { validateIfComponentParam(it) }))
        if (!deepLinkUri.host().isNullOrEmpty()) {
            validateIfComponentParam(deepLinkUri.host())
            node = node.addNode(Host(deepLinkUri.host()))
            if (deepLinkUri.pathSegments().isNullOrEmpty()) {
                node.match = UriMatch(deepLinkUri, matchIndex, annotatedElement, annotatedMethod)
            }
        }
        if (!deepLinkUri.pathSegments().isNullOrEmpty()) {
            for (pathSegment in deepLinkUri.pathSegments()) {
                validateIfComponentParam(pathSegment)
                validateIfConfigurablePathSegment(pathSegment)
                node = node.addNode(PathSegment(pathSegment))
            }
            node.match = UriMatch(deepLinkUri, matchIndex, annotatedElement, annotatedMethod)
        }
    }
}

data class Scheme(override val id: String) : TreeNode(id = id, metadata = NodeMetadata(MetadataMasks.ComponentTypeSchemeMask, id))

data class Host(override val id: String) : TreeNode(id = id, metadata = NodeMetadata(MetadataMasks.ComponentTypeHostMask, id))

data class PathSegment(override val id: String) : TreeNode(id = id, metadata = NodeMetadata(MetadataMasks.ComponentTypePathSegmentMask, id))

fun UByteArray.writeUIntAt(startIndex: Int, value: UInt) {
    val ubyte3: UByte = value.and(0x000000FFu).toUByte().toUByte()
    val ubyte2: UByte = value.shr(8).and(0x0000FFu).toUByte()
    val ubyte1: UByte = value.shr(16).and(0x00FFu).toUByte()
    val ubyte0: UByte = value.shr(24).and(0xFFu).toUByte()
    set(startIndex, ubyte0)
    set(startIndex + 1, ubyte1)
    set(startIndex + 2, ubyte2)
    set(startIndex + 3, ubyte3)
}

fun UByteArray.writeUShortAt(startIndex: Int, value: UShort) {
    val ubyte1: UByte = value.and(0x000000FFu).toUByte()
    val ubyte0: UByte = value.toUInt().shr(8).and(0x0000FFu).toUByte()
    set(startIndex, ubyte0)
    set(startIndex + 1, ubyte1)
}

const val configurablePathSegmentPrefix = "<"
const val configurablePathSegmentSuffix = ">"
const val componentParamPrefix = "{"
const val componentParamSuffix = "}"
