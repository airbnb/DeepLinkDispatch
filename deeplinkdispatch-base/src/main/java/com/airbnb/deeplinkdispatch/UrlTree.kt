package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex
import com.airbnb.deeplinkdispatch.base.MatchIndex.*
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

data class UriMatch(val uri: DeepLinkUri, val matchId: Int, val annotatedElement: String, val annotatedMethod: String?)

@kotlin.ExperimentalUnsignedTypes
open class TreeNode(open val id: String, val type: UByte, open val placeholder: Boolean = false) {

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
     * 0                                                    type
     * 1                                                    value length
     * 2..5                                                children length
     * 6..8                                                 match id
     * 8..(8+value length)                                  value
     * (8+value length)..((8+value length)+children length) children
     */
    fun toUByteArray(): UByteArray {
        val childrenByteArrays: List<UByteArray> = generateChildrenByteArrays()
        val valueByteArray = (if (placeholder) IDX_PLACEHOLDER + id else id).let { it.toByteArray(UTF_8).toUByteArray() }
        val header = generateHeader(type, valueByteArray, childrenByteArrays, match)
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

    private fun generateChildrenByteArrays(): List<UByteArray> = children.map { it.toUByteArray() }

    private fun generateHeader(type: UByte, value: UByteArray, children: List<UByteArray>? = null, match: UriMatch?): UByteArray {
        var childrenLength: Int = children?.sumBy { it.size } ?: 0
        return UByteArray(HEADER_LENGTH).apply {
            set(0, type)
            set(1, value.size.toUByte()) // If this is a placeholder we will only save one byte
            writeUIntAt(2, childrenLength.toUInt())
            writeUShortAt(6, if (match == null) MatchIndex.NO_MATH.toUShort() else match.matchId.toUShort())
        }
    }

}

private val MAX_EXPOT_STRING_SIZE = 50000

data class Root(override val id: String = "r") : TreeNode(ROOT_VALUE, TYPE_ROOT.toUByte()) {
    fun writeToOutoutStream(openOutputStream: OutputStream) {
        openOutputStream.write(this.toUByteArray().toByteArray())
    }

    /**
     * Convert the byte array into a string return as max 60k length sset of strings.
     */
    fun getStrings(): List<String> {
        return String(bytes = this.toUByteArray().toByteArray(), charset = Charset.forName(MatchIndex.MATCH_INDEX_ENCODING)).chunked(MAX_EXPOT_STRING_SIZE)
    }

    /**
     * Add the given DeepLinkUri to the the trie
     */
    fun addToTrie(matchIndex: Int, deeplinkUri: DeepLinkUri, annotatedElement: String, annotatedMethod: String?, uriPathPlaceholder: String?, uriPathPlaceholderDeclaredValue: String?) {
        var node = this.addNode(Scheme(deeplinkUri.scheme()))
        if (!deeplinkUri.host().isNullOrEmpty()) {
            node = node.addNode(Host(deeplinkUri.host(), hasPlaceholders(deeplinkUri.host())))
            if (deeplinkUri.pathSegments().isNullOrEmpty()) {
                node.match = UriMatch(deeplinkUri, matchIndex, annotatedElement, annotatedMethod)
            }
        }
        if (!deeplinkUri.pathSegments().isNullOrEmpty()) {
            for (pathSegment in deeplinkUri.pathSegments()) {
                node = node.addNode(PathSegment(pathSegment, hasPlaceholders(pathSegment), uriPathPlaceholder, uriPathPlaceholderDeclaredValue))
            }
            node.match = UriMatch(deeplinkUri, matchIndex, annotatedElement, annotatedMethod)
        }
    }

    private fun hasPlaceholders(pathSegment: String): Boolean {
        val placeholderStart = pathSegment.contains("{")
        val placeholderEnd = pathSegment.contains("}")
        require(!(placeholderStart xor placeholderEnd)) { "Placeholders need to be complete (need to contain { and })" }
        return placeholderStart && placeholderEnd
    }
}

data class Scheme(override val id: String) : TreeNode(id = id, type = TYPE_SCHEME.toUByte())

data class Host(override val id: String, override val placeholder: Boolean = false) : TreeNode(id = id, type = TYPE_HOST.toUByte())

data class PathSegment(override val id: String, override val placeholder: Boolean = false) : TreeNode(id = id, type = TYPE_PATH_SEGMENT.toUByte()) {
    constructor(id: String, placeholder: Boolean = false, uriPathPlaceholder: String?, uriPathPlaceholderDeclaredValue: String?) : this(
            if (uriPathPlaceholder != null &&
                    id == uriPathPlaceholder &&
                    uriPathPlaceholderDeclaredValue != null) {
                uriPathPlaceholderDeclaredValue
            } else {
                id
            }, placeholder)
}

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