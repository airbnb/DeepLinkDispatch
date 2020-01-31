package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import com.airbnb.deeplinkdispatch.base.MatchIndex.*
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

data class UriMatch(val uri: DeepLinkUri, val matchId: Int, val annotatedElement: String, val annotatedMethod: String?)

@kotlin.ExperimentalUnsignedTypes
open class TreeNode(open val id: String, val uriComponent: UByte) {

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
     * 0                                                    uri component type (e.g. scheme,
     * authority, path, query, fragment. Correspond to fields of [DeepLinkUri])
     * 1                                                    node value's transformation type, if any
     * 2                                                    value length
     * 3...6                                                 children length
     * 7...8                                                 match id
     * (e.g. no replacement (literal value), regex-constrained string part or whole replacement,
     * whole path segment replacement (mapping injected via [BaseDeepLinkDelegate] constructor)
     * 10..(10+value length)                                  value
     * (10+value length)..((10+value length)+children length) children
     */
    fun toUByteArray(): UByteArray {
        val childrenByteArrays: List<UByteArray> = generateChildrenByteArrays()
        val valueByteArray = id.toByteArray(UTF_8).toUByteArray()
        val transformationFlag = id.transformationType()
        val header = generateHeader(uriComponent, valueByteArray, childrenByteArrays, match, transformationFlag)
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
     * combinations/permutations of flags.
     */
    enum class TransformationType(val flag: UByte) {
        NoTransformation(2u),
        /**
         * String Insertion type means that a string can be added into the uri component at runtime.
         */
        StringInsertion(4u),
        /**
         * PathSegmentReplacement type represents a path segment which will lookup a dynamic (runtime) provided
         * replacement value when it is visited.
         */
        PathSegmentReplacement(8u)
    }

    /**
     * Transformation types:
     * @return an ASCII encoding as a character for the flag for whichever transformation type
     * applies.
     */
    private fun String.transformationType(): UByte = when {
        startsWith("%%%") && endsWith("%%%") -> {
            TransformationType.PathSegmentReplacement.flag
        }
        contains("{") && contains("}") -> {
            TransformationType.StringInsertion.flag
        }
        contains("%%%") -> {
            throw IllegalArgumentException("Malformed nodeValue: ${this@transformationType}! If it contains %%%" +
                    ", it must both start and end with %%%.")
        }
        contains("{") || contains("}") -> {
            throw IllegalArgumentException("Malformed nodeValue: ${this@transformationType}! If it contains {" +
                    " or }, it must start and end with them respectively.")
        }
        else -> {
            TransformationType.NoTransformation.flag
        }
    }

    private fun arrayLength(childArrays: List<UByteArray>, value: UByteArray, header: UByteArray): Int {
        return header.size + value.size + childArrays.sumBy { it.size }
    }

    private fun generateChildrenByteArrays(): List<UByteArray> = children.map { it.toUByteArray() }

    private fun generateHeader(uriComponent: UByte, value: UByteArray, children: List<UByteArray>? = null, match: UriMatch?, transformationFlag: UByte): UByteArray {
        val childrenLength: Int = children?.sumBy { it.size } ?: 0
        return UByteArray(HEADER_LENGTH).apply {
            set(0, uriComponent)
            set(1, transformationFlag)
            set(2, value.size.toUByte())
            writeUIntAt(3, childrenLength.toUInt())
            writeUShortAt(7, match?.matchId?.toUShort() ?: NO_MATCH.toUShort())
        }
    }
}


private const val MAX_EXPORT_STRING_SIZE = 50000

data class Root(override val id: String = "r") : TreeNode(ROOT_VALUE, COMPONENT_ROOT.toUByte()) {
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

data class Scheme(override val id: String) : TreeNode(id = id, uriComponent = COMPONENT_SCHEME.toUByte())

data class Host(override val id: String) : TreeNode(id = id, uriComponent = COMPONENT_HOST.toUByte())

data class PathSegment(override val id: String) : TreeNode(id = id, uriComponent = COMPONENT_PATH_SEGMENT.toUByte())

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