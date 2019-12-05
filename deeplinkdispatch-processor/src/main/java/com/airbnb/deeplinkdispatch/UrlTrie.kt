package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.HEADER_LENGTH
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.IDX_PLACEHOLDER
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.ROOT_VALUE
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_FRAGMENT
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_HOST
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_PASSWORD
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_PATH_SEGMENT
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_PORT
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_QUERY_NAME_VALUE
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_ROOT
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_SCHEME
import com.airbnb.deeplinkdispatch.base.MatchIndex.Companion.TYPE_USERNAME
import java.io.OutputStream
import kotlin.text.Charsets.UTF_8

data class UriMatch(val uri: DeepLinkUri, val matchId: Int)

@kotlin.ExperimentalUnsignedTypes
open class TrieNode(open val id: String = "", val type: UByte, open val placeholder: Boolean = false) {

    val children = mutableSetOf<TrieNode>()
    var match: UriMatch? = null
        set(value) {
            if (match != null) error("Ambiguous URI. Same match for two URIs ($match vs $value)") else field = value
        }

    fun addNode(node: TrieNode): TrieNode {
        return if (children.add(node)) node else children.find { it.equals(node) }!!
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
    fun toByteArray(): UByteArray {
        val childrenByteArrays: List<UByteArray> = generateChildrenByteArrays()
        val header = if (match != null) generateHeader(type, id, placeholder, childrenByteArrays, match!!.matchId.toUShort()) else generateHeader(type, id, placeholder, childrenByteArrays)
        val resultByteArray = UByteArray(arrayLength(
                childrenByteArrays,
                if (placeholder) ByteArray(1) { idx -> IDX_PLACEHOLDER.toByte() }.toString(UTF_8) else id,
                header
        ))
        header.copyInto(resultByteArray)
        var position = header.size
        if (placeholder) {
            resultByteArray[position] = IDX_PLACEHOLDER.toUByte()
            position++
        } else {
            with(id.toByteArray(UTF_8).toUByteArray()) {
                copyInto(resultByteArray, position)
                position += size
            }
        }
        for (childByteArray in childrenByteArrays) {
            childByteArray.copyInto(resultByteArray, position)
            position += childByteArray.size
        }

        return resultByteArray
    }

    private fun arrayLength(childArrays: List<UByteArray>, value: String, header: UByteArray): Int {
        var length = header.size + value.toByteArray(UTF_8).size
        childArrays.forEach { child ->
            length += child.size
        }
        return length
    }

    private fun generateChildrenByteArrays(): List<UByteArray> = children.map { it.toByteArray() }

    private fun generateHeader(type: UByte, value: String, placeholder: Boolean, children: List<UByteArray>? = null, matchId: UShort = UShort.MAX_VALUE): UByteArray {
        var childrenLength: UInt = 0u
        children?.forEach { child ->
            childrenLength += child.size.toUInt()
        }
        val header = UByteArray(HEADER_LENGTH)
        header.set(0, type)
        header.set(1, if (placeholder) 1.toUByte() else value.length.toUByte()) // If this is a placeholder we will only save one byte
        header.writeUIntAt(2, childrenLength)
        header.writeUShortAt(6, matchId)
        return header
    }

}

data class Root(val bla: String = "") : TrieNode(ROOT_VALUE, TYPE_ROOT.toUByte()) {
    fun writeToOutoutStream(openOutputStream: OutputStream) {
        openOutputStream.write(this.toByteArray().toByteArray())
    }
}

data class Scheme(override val id: String) : TrieNode(type = TYPE_SCHEME.toUByte())

data class Username(override val id: String) : TrieNode(type = TYPE_USERNAME.toUByte())

data class Password(override val id: String) : TrieNode(type = TYPE_PASSWORD.toUByte())

data class Host(override val id: String) : TrieNode(type = TYPE_HOST.toUByte())

data class Port(override val id: String) : TrieNode(type = TYPE_PORT.toUByte())
data class PathSegment(override val id: String, override val placeholder: Boolean = false) : TrieNode(type = TYPE_PATH_SEGMENT.toUByte())

data class QueryNameValue(override val id: String, val value: String, override val placeholder: Boolean = false) : TrieNode(type = TYPE_QUERY_NAME_VALUE.toUByte())

data class Fragment(override val id: String) : TrieNode(type = TYPE_FRAGMENT.toUByte())

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
