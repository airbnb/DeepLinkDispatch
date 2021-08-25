@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS", "EXPERIMENTAL_OVERRIDE")

package com.airbnb.deeplinkdispatch

import com.airbnb.deeplinkdispatch.base.MatchIndex.*
import com.airbnb.deeplinkdispatch.base.Utils.validateIfComponentParam
import com.airbnb.deeplinkdispatch.base.Utils.validateIfConfigurablePathSegment
import com.airbnb.deeplinkdispatch.base.chunkOnModifiedUtf8ByteSize
import java.io.OutputStream
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8

data class UriMatch(val type: MatchType, val uriTemplate: String, val annotatedClassFullyQualifiedName: String, val annotatedMethod: String?)

enum class MatchType(val flagValue: UByte) {
    Activity(0u), Method(1u), Handler(2u);

    companion object {
        @JvmStatic
        fun fromInt(readOneByteAsInt: Int): MatchType {
            return values().firstOrNull { it.flagValue.toInt() == readOneByteAsInt }
                ?: error("Invalid flag value: $readOneByteAsInt")
        }
    }
}

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
     * 0                                                      [NodeMetadata] flags; 1 byte
     * 1                                                      length of value sub-array
     * 2..3                                                   length of match sub-array
     * 4...7                                                  length of node's children sub-array
     * 8..(8+value length)                                    actual value sub-array
     * (8+value length)..
     * ((8+value length)+children length)                     match data (can be 0 length)
     * (8+value length+match data length)..
     * ((8+value length+match data length)+children length)   actual children sub-array
     */
    fun toUByteArray(): UByteArray {
        val childrenByteArrays: List<UByteArray> = generateChildrenByteArrays()
        val valueByteArray = serializedId().toByteArray(UTF_8).toUByteArray()
        val matchByteArray = matchByteArray(match)
        val header = generateHeader(metadata, valueByteArray, matchByteArray, childrenByteArrays)
        val resultByteArray = UByteArray(arrayLength(
                childArrays = childrenByteArrays,
                valueArray = valueByteArray,
                matchArray = matchByteArray,
                headerArray = header
        ))
        header.copyInto(resultByteArray)
        var position = header.size
        position = valueByteArray.copyIntoPosition(resultByteArray, position)
        position = matchByteArray.copyIntoPosition(resultByteArray, position)
        for (childByteArray in childrenByteArrays) {
            position = childByteArray.copyIntoPosition(resultByteArray, position)
        }
        return resultByteArray
    }

    private fun UByteArray.copyIntoPosition(targetByteArray: UByteArray, position: Int): Int {
        return with(targetByteArray) {
            this@copyIntoPosition.copyInto(this, position)
            position + this@copyIntoPosition.size
        }
    }

    private fun arrayLength(childArrays: List<UByteArray>, valueArray: UByteArray, matchArray: UByteArray, headerArray: UByteArray): Int {
        return headerArray.size + valueArray.size + matchArray.size + childArrays.sumOf { it.size }
    }

    // Make sure we match concrete matches before placeholders or configurable path segments
    private fun generateChildrenByteArrays(): List<UByteArray> = children.sortedWith(compareBy({ it.metadata.isConfigurablePathSegment }, { it.metadata.isComponentParam }, { it.id })).map { it.toUByteArray() }

    private fun generateHeader(metadata: NodeMetadata, value: UByteArray, matchByteArray: UByteArray, children: List<UByteArray>? = null): UByteArray {
        val childrenLength: Int = children?.sumOf { it.size } ?: 0
        return UByteArray(HEADER_LENGTH).apply {
            set(0, metadata.metadata.toUByte()) // flag
            set(HEADER_NODE_METADATA_LENGTH, value.size.toUByte()) // value length
            writeUShortAt(startIndex = HEADER_NODE_METADATA_LENGTH + HEADER_VALUE_LENGTH,
                    value = matchByteArray.size.toUShort()) // match length
            writeUIntAt(startIndex = HEADER_NODE_METADATA_LENGTH + HEADER_VALUE_LENGTH + HEADER_MATCH_LENGTH,
                    value = childrenLength.toUInt()) // children length
        }
    }
}


private const val MAX_CODE_STRING_BYTE_SIZE = 65535 // (2^16)-1 as the chunk needs to be 16 bit addressable.

data class Root(override val id: String = "r") : TreeNode(ROOT_VALUE, NodeMetadata(MetadataMasks.ComponentTypeRootMask, id)) {
    fun writeToOutoutStream(openOutputStream: OutputStream) {
        openOutputStream.write(this.toUByteArray().toByteArray())
    }

    /**
     * Convert the byte array into a string return as max 65k length set of strings.
     *
     * The Java compiler saves the string constant as Modified UTF-8, with the binary representation
     * of this not allowed to be larger than 65535. So we chunk our string exactly to that size.
     */
    fun getStrings(): List<CharSequence> {
        return String(bytes = this.toUByteArray().toByteArray(), charset = Charset.forName(MATCH_INDEX_ENCODING)).chunkOnModifiedUtf8ByteSize(MAX_CODE_STRING_BYTE_SIZE)
    }

    /**
     * Add the given DeepLinkUri to the the trie
     */
    fun addToTrie(matchType: MatchType, deepLinkUriTemplate: String, annotatedClassFullyQualifiedName: String, annotatedMethod: String?) {
        val deepLinkUri = DeepLinkUri.parseTemplate(deepLinkUriTemplate)
        var node = this.addNode(Scheme(deepLinkUri.scheme().also { validateIfComponentParam(it) }))
        if (!deepLinkUri.host().isNullOrEmpty()) {
            validateIfComponentParam(deepLinkUri.host())
            node = node.addNode(Host(deepLinkUri.host()))
            if (deepLinkUri.pathSegments().isNullOrEmpty()) {
                node.match = UriMatch(matchType,deepLinkUriTemplate, annotatedClassFullyQualifiedName, annotatedMethod)
            }
        }
        if (!deepLinkUri.pathSegments().isNullOrEmpty()) {
            for (pathSegment in deepLinkUri.pathSegments()) {
                validateIfComponentParam(pathSegment)
                validateIfConfigurablePathSegment(pathSegment)
                node = node.addNode(PathSegment(pathSegment))
            }
            node.match = UriMatch(matchType, deepLinkUriTemplate, annotatedClassFullyQualifiedName, annotatedMethod)
        }
    }
}

data class Scheme(override val id: String) : TreeNode(id = id, metadata = NodeMetadata(MetadataMasks.ComponentTypeSchemeMask, id))

data class Host(override val id: String) : TreeNode(id = id, metadata = NodeMetadata(MetadataMasks.ComponentTypeHostMask, id))

data class PathSegment(override val id: String) : TreeNode(id = id, metadata = NodeMetadata(MetadataMasks.ComponentTypePathSegmentMask, id))

/**
 * Match data byte array format is:
 * 0                                                               match type
 * 1..1 url                                                        url template length
 * 3..(2+url template length)                                      url template
 * (3+url template length)..
 * (3+url template length)+2                                       classname length
 * (3+url template length)+2..
 * (3+url template length)+2+classname length                      class name
 * (3+url template length)+2+classname length..
 * (3+url template length)+2+classname length+1                    method name length (can be 0)
 * (3+url template length)+2+classname length+1..
 * (3+url template length)+2+classname length+1+method name length method name
 */
fun matchByteArray(match: UriMatch?): UByteArray {
    if (match == null) return UByteArray(0)

    val uriTemplateByteArray = match.uriTemplate.toByteArray(UTF_8).toUByteArray()
    val classNameByteArray = match.annotatedClassFullyQualifiedName.toByteArray(UTF_8).toUByteArray()
    val methodNameByteArray = match.annotatedMethod?.toByteArray(UTF_8)?.toUByteArray()
            ?: UByteArray(0)
    return UByteArray(
        MATCH_DATA_TYPE_LENGTH + MATCH_DATA_URL_TEMPLATE_LENGTH + uriTemplateByteArray.size
            + MATCH_DATA_CLASS_LENGTH + classNameByteArray.size
            + MATCH_DATA_METHOD_LENGTH + methodNameByteArray.size)
            .apply {
                var position = 0
                // Type
                this[position] = match.type.flagValue
                position += MATCH_DATA_TYPE_LENGTH
                // Uri template
                writeUShortAt(startIndex = position, value = uriTemplateByteArray.size.toUShort())
                position += MATCH_DATA_URL_TEMPLATE_LENGTH
                uriTemplateByteArray.copyInto(destination = this, destinationOffset = position)
                position += uriTemplateByteArray.size

                // Class name
                writeUShortAt(startIndex = position, value = classNameByteArray.size.toUShort())
                position += MATCH_DATA_CLASS_LENGTH
                classNameByteArray.copyInto(destination = this, destinationOffset = position)
                position += classNameByteArray.size

                // method
                this.set(position, methodNameByteArray.size.toUByte())
                position += MATCH_DATA_METHOD_LENGTH
                if (methodNameByteArray.isNotEmpty()) methodNameByteArray.copyInto(destination = this, destinationOffset = position)
            }
}

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

const val configurablePathSegmentPrefix = "<"
const val configurablePathSegmentPrefixChar = configurablePathSegmentPrefix.get(0)
const val configurablePathSegmentSuffix = ">"
const val configurablePathSegmentSuffixChar = configurablePathSegmentSuffix.get(0)
const val componentParamPrefix = "{"
const val componentParamPrefixChar = componentParamPrefix.get(0)
const val componentParamSuffix = "}"
const val componentParamSuffixChar = componentParamSuffix.get(0)
