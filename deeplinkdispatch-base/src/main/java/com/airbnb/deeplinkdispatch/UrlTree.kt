@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS", "EXPERIMENTAL_OVERRIDE")

package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import com.airbnb.deeplinkdispatch.base.MatchIndex.*
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

data class UriMatch(val uri: DeepLinkUri, val matchId: Int, val annotatedElement: String, val annotatedMethod: String?)

@kotlin.ExperimentalUnsignedTypes
open class TreeNode(open val id: String, internal val uriComponentType: NodeMetadata) {

    val children = mutableSetOf<TreeNode>()
    var match: UriMatch? = null
        set(value) {
            if (field != null) error("Ambiguous URI. Same match for two URIs ($field vs $value)") else field = value
        }

    fun addNode(node: TreeNode): TreeNode {
        return if (children.add(node)) node else children.first { it == node }
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
        val valueByteArray = id.toByteArray(UTF_8).toUByteArray()
        val transformationFlag = id.transformationType()
        val header = generateHeader(uriComponentType.flag, valueByteArray, childrenByteArrays, match, transformationFlag)
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

    /**
     * Use a UByte for [flag] so that we can easily specify and read different
     * combinations/permutations of flags with a bit mask.
     */
    enum class NodeMetadata(val flag: UByte) {
        /**
         * Root is just used to start the tree. It does not correspond to a normal URI component.
         */
        IsComponentTypeRoot(1u),
        IsComponentTypeScheme(2u),
        IsComponentTypeHost(4u),
        IsComponentTypePathSegment(8u),
        /**
         * Component params are uri components (host, path segment) that also contain a param. The
         * param both matches a range of values and can record the value from said range.
         */
        IsComponentParam(16u),
        /**
         * IsConfigurablePathSegment type represents a path segment which will lookup a dynamic (runtime) provided
         * replacement value when it is visited. The replacement value will replace the entire path
         * segment (content between slashes).
         */
        IsConfigurablePathSegment(32u)
    }

    /**
     * Transformation types:
     * @return an ASCII encoding as a character for the flag for whichever transformation type
     * applies.
     */
    private fun String.transformationType(): UByte {
        return when {
            startsWith(pathSegmentEnclosingSequence) && endsWith(pathSegmentEnclosingSequence) -> {
                NodeMetadata.IsConfigurablePathSegment.flag
            }
            contains("{") && contains("}") -> {
                NodeMetadata.IsComponentParam.flag
            }
            else -> {
                0u
            }
        }
    }

    private fun arrayLength(childArrays: List<UByteArray>, value: UByteArray, header: UByteArray): Int {
        return header.size + value.size + childArrays.sumBy { it.size }
    }

    private fun generateChildrenByteArrays(): List<UByteArray> = children.map { it.toUByteArray() }

    private fun generateHeader(uriComponentType: UByte, value: UByteArray, children: List<UByteArray>? = null, match: UriMatch?, transformationType: UByte): UByteArray {
        val childrenLength: Int = children?.sumBy { it.size } ?: 0
        return UByteArray(HEADER_LENGTH).apply {
            set(0, calculateNodeMetadata(uriComponentType, transformationType))
            set(1, value.size.toUByte())
            writeUIntAt(2, childrenLength.toUInt())
            writeUShortAt(6, match?.matchId?.toUShort() ?: NO_MATCH.toUShort())
        }
    }

    /**
     * Derive node's metadata bit flags. It will be stored in the first byte of the node's header.
     */
    private fun calculateNodeMetadata(uriComponentType: UByte, transformationType: UByte): UByte =
            uriComponentType or transformationType
}


private const val MAX_EXPORT_STRING_SIZE = 50000

data class Root(override val id: String = "r") : TreeNode(ROOT_VALUE, NodeMetadata.IsComponentTypeRoot) {
    fun writeToOutoutStream(openOutputStream: OutputStream) {
        openOutputStream.write(this.toUByteArray().toByteArray())
    }

    /**
     * Convert the byte array into a string return as max 60k length sset of strings.
     */
    fun getStrings(): List<String> {
        return String(bytes = this.toUByteArray().toByteArray(), charset = Charset.forName(MatchIndex.MATCH_INDEX_ENCODING)).chunked(MAX_EXPORT_STRING_SIZE)
    }

    /**
     * Add the given DeepLinkUri to the the trie
     */
    fun addToTrie(matchIndex: Int, deepLinkUri: DeepLinkUri, annotatedElement: String, annotatedMethod: String?) {
        var node = this.addNode(Scheme(deepLinkUri.scheme()))
        if (!deepLinkUri.host().isNullOrEmpty()) {
            node = node.addNode(Host(deepLinkUri.host()))
            if (deepLinkUri.pathSegments().isNullOrEmpty()) {
                node.match = UriMatch(deepLinkUri, matchIndex, annotatedElement, annotatedMethod)
            }
        }
        if (!deepLinkUri.pathSegments().isNullOrEmpty()) {
            for (pathSegment in deepLinkUri.pathSegments()) {
                node = node.addNode(PathSegment(pathSegment))
            }
            node.match = UriMatch(deepLinkUri, matchIndex, annotatedElement, annotatedMethod)
        }
    }
}

data class Scheme(override val id: String) : TreeNode(id = id, uriComponentType = NodeMetadata.IsComponentTypeScheme)

data class Host(override val id: String) : TreeNode(id = id, uriComponentType = NodeMetadata.IsComponentTypeHost)

data class PathSegment(override val id: String) : TreeNode(id = id, uriComponentType = NodeMetadata.IsComponentTypePathSegment)

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

const val pathSegmentEnclosingSequence = "%%%"