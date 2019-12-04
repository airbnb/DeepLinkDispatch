package com.airbnb.deeplinkdispatch.base;

import com.airbnb.deeplinkdispatch.UrlElement;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This is a wrapper class around the byte array matgch index
 */
public class MatchIndex {

  /**
   * Lenght of header elements in bytes
   */
  private static final int HEADER_TYPE_LENGTH = 1;
  private static final int HEADER_VALUE_LENGHT = 1;
  private static final int HEADER_CHILDREN_LENGTH = 4;
  private static final int HEADER_MATCH_ID_LENGTH = 2;

  public static final int HEADER_LENGTH = HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT + HEADER_CHILDREN_LENGTH + HEADER_MATCH_ID_LENGTH;

  /**
   * Type constants used in match index
   */
  public static final byte TYPE_ROOT = (byte) 0;
  public static final byte TYPE_SCHEME = (byte) 1;
  public static final byte TYPE_USERNAME = (byte) 2;
  public static final byte TYPE_PASSWORD = (byte) 3;
  public static final byte TYPE_HOST = (byte) 4;
  public static final byte TYPE_PORT = (byte) 5;
  public static final byte TYPE_PATH_SEGMENT = (byte) 6;
  public static final byte TYPE_QUERY_NAME_VALUE = (byte) 7;
  public static final byte TYPE_FRAGMENT = (byte) 8;

  /**
   * Used as a value placeholder to indicate the value is a placeholder
   * e.g. in dld://host/{param1} "{param1}" would be encoded as this character.
   */
  public static final byte IDX_PLACEHOLDER = 0x1a; // ASCII substitute

  @NotNull
  public static final String ROOT_VALUE = "r";

  private byte[] byteArray;

  public MatchIndex(byte[] byteArray) {
    this.byteArray = byteArray;
  }

  public int matchUri(List<UrlElement> elements, int elementIndex, int elementStartPosition, int parentBoundryPos) {
    int matchIndex = -1;
    int currentElementStartPosition = elementStartPosition;
    do {
      UrlElement urlElement = elements.get(elementIndex);
      if (compareValue(currentElementStartPosition, urlElement.getType(), urlElement.getValue())){
        if (elementIndex < elements.size()-1) {
          // If value matched we need to explore this elements children next.
          matchIndex = matchUri(elements, elementIndex + 1, getChildrenPos(currentElementStartPosition), getElementBoundaryPos(currentElementStartPosition));
        } else {
          matchIndex = getMatchIndex(currentElementStartPosition);
        }
      }
      if (matchIndex != -1){
        return matchIndex;
      }
      currentElementStartPosition = getNextElementStartPosition(currentElementStartPosition, parentBoundryPos);
    } while (currentElementStartPosition != -1);
    return -1;
  }

  /**
   * @param elementStartPos   The start position of the element to compare
   * @param type  The type of the value to compare
   * @param value The value of the value to compare
   * @return true if the type, length and value of the element staring at elementStartPos is the same as
   * the value given in in the parameter. false otherwise.
   */
  private boolean compareValue(int elementStartPos, byte type, byte[] value) {
    // Placeholder always matches
    if (byteArray[elementStartPos + HEADER_LENGTH] == IDX_PLACEHOLDER){
      return true;
    }
    if ((byteArray[elementStartPos] != type || getValueLength(elementStartPos) != value.length)) {
      return false;
    }
    final int valueOffset = elementStartPos + HEADER_LENGTH;
    for (int i = 0; i < value.length; i++) {
      if (byteArray[valueOffset + i] != value[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Get the next entries position, or -1 if there are no further entries.
   *
   * @param elementStartPos           The start postion of the current element.
   * @param parentBoundaryPos The parent elements boundry (i.e. the first elementStartPos that is not part of the parent element anhymore)
   * @return
   */
  private int getNextElementStartPosition(int elementStartPos, int parentBoundaryPos) {
    int nextElementPos = getElementBoundaryPos(elementStartPos);
    if (nextElementPos == parentBoundaryPos) {
      // This was the last element
      return -1;
    } else if (nextElementPos > parentBoundaryPos) {
      // TODO Remove this check for prod
      throw new IllegalStateException("Element size error at index "+elementStartPos);
    }
    else {
      return nextElementPos;
    }
  }

  /**
   * The elements boundary is the first elementStartPos that is not part of the parent element anymore.
   *
   * @param elementStartPos
   * @return
   */
  private int getElementBoundaryPos(int elementStartPos) {
    return elementStartPos + HEADER_LENGTH + getValueLength(elementStartPos) + getChildrenLenght(elementStartPos);
  }

  private int getChildrenPos(int elementStartPos) {
    return elementStartPos + HEADER_LENGTH + getValueLength(elementStartPos);
  }

  private int getValueLength(int elementStartPos) {
    return readOneByteAsInt(elementStartPos + HEADER_TYPE_LENGTH);
  }

  private int getChildrenLenght(int elementStartPos) {
    return readFourBytesAsInt(elementStartPos + HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT);
  }

  private int getMatchIndex(int elementStartPos) {
    return readTwoBytesAsInt(elementStartPos + HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT + HEADER_CHILDREN_LENGTH);
  }

  public int length() {
    return byteArray.length;
  }
  
  private int readOneByteAsInt(int pos) {
    return byteArray[pos] & 0xFF;
  }

  private int readTwoBytesAsInt(int pos) {
    return (byteArray[pos] & 0xFF) << 8 |
        (byteArray[pos + 1] & 0xFF);
  }

  private int readFourBytesAsInt(int pos) {
    return (byteArray[pos] & 0xFF) << 24 |
        (byteArray[pos + 1] & 0xFF) << 16 |
        (byteArray[pos + 2] & 0xFF) << 8 |
        (byteArray[pos + 3] & 0xFF);
  }

  /**
   * Get filename for match index.
   *
   * @param moduleName
   * @return
   */
  public static String getMatchIdxFileName(String moduleName) {
    return ("dld_match_" + moduleName + ".idx").toLowerCase();
  }

}
