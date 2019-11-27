package com.airbnb.deeplinkdispatch

import java.lang.StringBuilder
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

data class UriMatch(val uri: DeepLinkUri, val matchId: Int)

open class TrieNode(val id: String, val type: UByte) {

    val children = mutableSetOf<TrieNode>()
    var matchId: UriMatch? = null
        set(value) {
            if (matchId != null) error("Ambiguos URI. Same match for two URIs ($matchId vs $value)") else field = value
        }

    fun addNode(node: TrieNode): TrieNode {
        return if (children.add(node)) node else children.find { it.equals(node) }!!
    }

//    fun queryString(): String {
//        var queryString: String = ""
//        children.forEach { child ->
//            queryString += child.queryString()
//        }
//        queryString = children.joinToString(separator = RECORD_DIVIDER, transform = { it.type + it.id }, postfix = if (children.isEmpty()) "" else NODE_DIVIDER) + queryString
//        return queryString
//    }

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
        val header = generateHeader(type, id, childrenByteArrays)
        val resultByteArray = UByteArray(arrayLength(
                childrenByteArrays,
                id,
                header
        ))
        header.copyInto(resultByteArray)
        var position = header.size
        with(id.toByteArray(UTF_8).toUByteArray()) {
            copyInto(resultByteArray, position)
            position += size
        }
        for (childByteArray in childrenByteArrays) {
            childByteArray.copyInto(resultByteArray, position)
            position += childByteArray.size
        }

        return resultByteArray
    }

    private fun arrayLength(childArrays: List<UByteArray>, value: String, header: UByteArray): Int {
        var length = header.size + value.toByteArray(Charsets.UTF_8).size
        childArrays.forEach { child ->
            length += child.size
        }
        return length
    }

    private fun generateChildrenByteArrays(): List<UByteArray> = children.map { it.toByteArray() }

    private fun generateHeader(type: UByte, value: String, children: List<UByteArray>? = null, matchId: UShort = UShort.MAX_VALUE): UByteArray {
        var childrenLength: UInt = 0u
        children?.forEach { child ->
            childrenLength += child.size.toUInt()
        }
        val header = UByteArray(1 + 1 + 4 + 2)
        header.set(0, type)
        header.set(1, value.length.toUByte())
        header.writeUIntAt(2, childrenLength)
        header.writeUShortAt(6, matchId)
        return header
    }

    companion object {
//        val RECORD_DIVIDER = "_RECORD_"//30.toChar().toString() // (unit separator)
//        val NODE_DIVIDER = "_NODE_"//31.toChar().toString() // (unit separator)

        val TYPE_ROOT: UByte = 0u
        val TYPE_SCHEME: UByte = 1u
        val TYPE_USERNAME: UByte = 2u
        val TYPE_PASSWORD: UByte = 3u
        val TYPE_HOST: UByte = 4u
        val TYPE_PORT: UByte = 5u
        val TYPE_PATH_SEGMENT: UByte = 6u
        val TYPE_QUERY_NAME_VALUE: UByte = 7u
        val TYPE_FRAGMENT: UByte = 8u
    }
}

data class Root(val bla: String = "") : TrieNode("root", TYPE_ROOT) {
    fun toJavaByteArrayString(): String {
        println("Array length: $")
        var bytes = 0
        return "new byte[] " +
                children.joinToString(
                        ", ", "{", "}", -1, "...", ({ child ->
                    child.toByteArray().joinToString(separator = ", ", transform = ({ byte ->
                        bytes++
                        "(byte) $byte"
                    }))
                })).also { println ("Bytes written: $bytes") }
    }
}

data class Scheme(val scheme: String) : TrieNode(scheme, TYPE_SCHEME)

data class Username(val username: String) : TrieNode(username, TYPE_USERNAME)

data class Password(val password: String) : TrieNode(password, TYPE_PASSWORD)

data class Host(val host: String) : TrieNode(host, TYPE_HOST)

data class Port(val port: String) : TrieNode(port, TYPE_PORT)
data class PathSegment(val pathSegment: String, val placeholder: Boolean = false) : TrieNode(pathSegment, TYPE_PATH_SEGMENT)

data class QueryNameValue(val name: String, val value: String, val placeholder: Boolean = false) : TrieNode(name, TYPE_QUERY_NAME_VALUE)

data class Fragment(val fragemnt: String) : TrieNode(fragemnt, TYPE_FRAGMENT)

fun UByteArray.writeUIntAt(startIndex: Int, value: UInt) {
    val ubyte3: UByte = value.and(0x000000FFu).toUByte()
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
