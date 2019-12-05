package com.airbnb.deeplinkdispatch.base

import com.airbnb.deeplinkdispatch.UrlElement

import java.util.ArrayList

/**
 * This is a wrapper class around the byte array matgch index
 */
@ExperimentalUnsignedTypes
class MatchIndex(val byteArray: ByteArray) {

    @JvmOverloads
    fun matchUri(elements: List<UrlElement>, placeholders: MutableList<String> = mutableListOf(), elementIndex: Int, elementStartPosition: Int, parentBoundryPos: Int): Match? {
        var match: Match? = null
        var currentElementStartPosition = elementStartPosition
        do {
            val (type, value) = elements[elementIndex]
            val compareresult = compareValue(currentElementStartPosition, type, value)
            if (compareresult == CompareResult.EQUAL || compareresult == CompareResult.PLACEHOLDER) {
                if (compareresult == CompareResult.PLACEHOLDER) {
                    placeholders.add(String(value.toByteArray()))
                }
                if (elementIndex < elements.size - 1) {
                    // If value matched we need to explore this elements children next.
                    val childrenPos = getChildrenPos(currentElementStartPosition)
                    if (childrenPos != -1) {
                        match = matchUri(elements, ArrayList(placeholders), elementIndex + 1, childrenPos, getElementBoundaryPos(currentElementStartPosition))
                    }
                } else {
                    match = Match(getMatchIndex(currentElementStartPosition),placeholders)
                }
            }
            if (match != null) {
                return match
            }
            currentElementStartPosition = getNextElementStartPosition(currentElementStartPosition, parentBoundryPos)
        } while (currentElementStartPosition != -1)
        return match
    }

    /**
     * @param elementStartPos The start position of the element to compare
     * @param type            The type of the value to compare
     * @param value           The value of the value to compare
     * @return true if the type, length and value of the element staring at elementStartPos is the same as
     * the value given in in the parameter. false otherwise.
     */
    private fun compareValue(elementStartPos: Int, type: UByte, value: UByteArray): CompareResult {
        // Placeholder always matches
        if (byteArray[elementStartPos + HEADER_LENGTH].toUByte() == IDX_PLACEHOLDER) {
            return CompareResult.PLACEHOLDER
        }
        if (byteArray[elementStartPos] .toUByte()!= type || getValueLength(elementStartPos) != value.size) {
            return CompareResult.NOT_EQUAL
        }
        val valueOffset = elementStartPos + HEADER_LENGTH
        for (i in value.indices) {
            if (byteArray[valueOffset + i].toUByte() != value[i]) {
                return CompareResult.NOT_EQUAL
            }
        }
        return CompareResult.EQUAL
    }

    /**
     * Get the next entries position, or -1 if there are no further entries.
     *
     * @param elementStartPos   The start postion of the current element.
     * @param parentBoundaryPos The parent elements boundry (i.e. the first elementStartPos that is not part of the parent element anhymore)
     * @return
     */
    private fun getNextElementStartPosition(elementStartPos: Int, parentBoundaryPos: Int): Int {
        val nextElementPos = getElementBoundaryPos(elementStartPos)
        return if (nextElementPos == parentBoundaryPos) {
            // This was the last element
            -1
        } else if (nextElementPos > parentBoundaryPos) {
            // TODO Remove this check for prod
            throw IllegalStateException("Element size error at index $elementStartPos")
        } else {
            nextElementPos
        }
    }

    /**
     * The elements boundary is the first elementStartPos that is not part of the parent element anymore.
     *
     * @param elementStartPos
     * @return
     */
    private fun getElementBoundaryPos(elementStartPos: Int): Int {
        return elementStartPos + HEADER_LENGTH + getValueLength(elementStartPos) + getChildrenLenght(elementStartPos)
    }

    /**
     * Get the position of the children element of the element starting at elementStartPos.
     *
     * @param elementStartPos The start position of the element to get the children for
     * @return children pos or -1 if there are no children.
     */
    private fun getChildrenPos(elementStartPos: Int): Int {
        return if (getChildrenLenght(elementStartPos) == 0) {
            -1
        } else {
            elementStartPos + HEADER_LENGTH + getValueLength(elementStartPos)
        }
    }

    private fun getValueLength(elementStartPos: Int): Int {
        return readOneByteAsInt(elementStartPos + HEADER_TYPE_LENGTH)
    }

    private fun getChildrenLenght(elementStartPos: Int): Int {
        return readFourBytesAsInt(elementStartPos + HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT)
    }

    private fun getMatchIndex(elementStartPos: Int): Int {
        return readTwoBytesAsInt(elementStartPos + HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT + HEADER_CHILDREN_LENGTH)
    }

    fun length(): Int {
        return byteArray.size
    }

    private fun readOneByteAsInt(pos: Int): Int {
        return byteArray[pos].toInt() and 0xFF
    }

    private fun readTwoBytesAsInt(pos: Int): Int {
        return byteArray[pos].toInt() and 0xFF shl 8 or (byteArray[pos + 1].toInt() and 0xFF)
    }

    private fun readFourBytesAsInt(pos: Int): Int {
        return byteArray[pos].toInt() and 0xFF shl 24 or (
                byteArray[pos + 1].toInt() and 0xFF shl 16) or (
                byteArray[pos + 2].toInt() and 0xFF shl 8) or
                (byteArray[pos + 3].toInt() and 0xFF)
    }

    companion object {

        /**
         * Lemngth of header elements in bytes
         */
        private val HEADER_TYPE_LENGTH = 1
        private val HEADER_VALUE_LENGHT = 1
        private val HEADER_CHILDREN_LENGTH = 4
        private val HEADER_MATCH_ID_LENGTH = 2

        val HEADER_LENGTH = HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT + HEADER_CHILDREN_LENGTH + HEADER_MATCH_ID_LENGTH

        /**
         * Type constants used in match index
         */
        val TYPE_ROOT = 0.toUByte()
        val TYPE_SCHEME = 1.toUByte()
        val TYPE_USERNAME = 2.toUByte()
        val TYPE_PASSWORD = 3.toUByte()
        val TYPE_HOST = 4.toUByte()
        val TYPE_PORT = 5.toUByte()
        val TYPE_PATH_SEGMENT = 6.toUByte()
        val TYPE_QUERY_NAME_VALUE = 7.toUByte()
        val TYPE_FRAGMENT = 8.toUByte()

        /**
         * Used as a value placeholder to indicate the value is a placeholder
         * e.g. in dld://host/{param1} "{param1}" would be encoded as this character.
         */
        val IDX_PLACEHOLDER: UByte = 0x1au // ASCII substitute

        val ROOT_VALUE = "r"

        /**
         * Get filename for match index.
         *
         * @param moduleName
         * @return
         */
        fun getMatchIdxFileName(moduleName: String): String {
            return "dld_match_$moduleName.idx".toLowerCase()
        }
    }

}

data class Match(val matchIndex: Int, val placeholders: List<String>)

enum class CompareResult {
    EQUAL,
    PLACEHOLDER,
    NOT_EQUAL
}
