package com.airbnb.deeplinkdispatch.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.deeplinkdispatch.UrlElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a wrapper class around the byte array matgch index.
 * <p>
 * Byte array format is:
 *
 * <pre><
 * 1 byte  2 bytes       4 bytes                     2 bytes       n bytes       n bytes
 * +------+-------------+---------------------------+-------------+-------------+--------------
 * |      |             |                           |             |             |
 * | Type |Value length | Children length           | Match idx   | Value       | Children
 * |      |             |                           |             |             |
 * +------+-------------+---------------------------+-------------+-------------+--------------
 *
 * <----------------------+  9 byte header  +--------------------->             +------+-------
 *                                                                              |      |
 *                                                                              | Type |Value
 *                                                                              |      |
 *                                                                              +------+-------
 * -------------------------------------------------------------------------------------------+
 *                                                                                            |
 *                                                                                            |
 *                                                                                            |
 * -------------------------------------------------------------------------------------------+
 *
 * -------+---------------------------+-------------+-------------+---------------------------+
 *        |             |                           |             |                           |
 * length | Children length           | Match idx   | Value       | Children                  |
 *        |             |                           |             |                           |
 * -------+---------------------------+-------------+-------------+---------------------------+
 * </pre>
 */
public class MatchIndex {

  /**
   * Encoding used for serialization
   */
  @NonNull
  public static final String MATCH_INDEX_ENCODING = "ISO_8859_1";

  /**
   * Lenght of header elements in bytes
   */
  private static final int HEADER_TYPE_LENGTH = 1;
  private static final int HEADER_VALUE_LENGHT = 1;
  private static final int HEADER_CHILDREN_LENGTH = 4;
  private static final int HEADER_MATCH_ID_LENGTH = 2;

  public static final int HEADER_LENGTH = HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT
      + HEADER_CHILDREN_LENGTH + HEADER_MATCH_ID_LENGTH;

  /**
   * Type constants used in match index
   */
  public static final byte TYPE_ROOT = (byte) 0;
  public static final byte TYPE_SCHEME = (byte) 1;
  public static final byte TYPE_HOST = (byte) 2;
  public static final byte TYPE_PATH_SEGMENT = (byte) 3;

  /**
   * Used as a value placeholder to indicate the value is a placeholder
   * e.g. in dld://host/{param1} "{param1}" would be encoded as this character.
   */
  @NonNull
  public static final char IDX_PLACEHOLDER = 0x1a; // ASCII substitute

  @NonNull
  public static final String ROOT_VALUE = "r";

  // Used to separate param and param value in comapre return value (record separator)
  @NonNull
  public static final String MATCH_PARAM_DIVIDER_CHAR = String.valueOf((char) 0x1e);

  @NonNull
  private byte[] byteArray;

  public MatchIndex(@NonNull byte[] byteArray) {
    this.byteArray = byteArray;
  }

  public Match matchUri(@NonNull List<UrlElement> elements, @Nullable Map<String, String>
      placeholders, int elementIndex, int elementStartPosition, int parentBoundryPos) {
    Match match = null;
    int currentElementStartPosition = elementStartPosition;
    do {
      UrlElement urlElement = elements.get(elementIndex);
      String compareResult = compareValue(currentElementStartPosition, urlElement.getType(),
          urlElement.getValue());
      if (compareResult != null) {
        // Need to hand down a new Hashmap, but only if we already have one.
        Map<String, String> placeholdersOutput = placeholders == null ? null : placeholders;
        if (!compareResult.isEmpty()) {
          if (placeholdersOutput == null) {
            placeholdersOutput = new HashMap<>(placeholders != null ? placeholders
                : Collections.<String, String>emptyMap());
          }
          String[] compareParams = compareResult.split(MATCH_PARAM_DIVIDER_CHAR);
          placeholdersOutput.put(compareParams[0], compareParams[1]);
        }
        if (elementIndex < elements.size() - 1) {
          // If value matched we need to explore this elements children next.
          int childrenPos = getChildrenPos(currentElementStartPosition);
          if (childrenPos != -1) {
            match = matchUri(elements, placeholdersOutput, elementIndex + 1,
                childrenPos, getElementBoundaryPos(currentElementStartPosition));
          }
        } else {
          match = new Match(getMatchIndex(currentElementStartPosition), placeholdersOutput == null
              ? new HashMap<String, String>(0) : placeholdersOutput);
        }
      }
      if (match != null) {
        return match;
      }
      currentElementStartPosition = getNextElementStartPosition(currentElementStartPosition,
          parentBoundryPos);
    } while (currentElementStartPosition != -1);
    return null;
  }

  /**
   * @param elementStartPos The start position of the element to compare
   * @param type            The type of the value to compare
   * @param value           The value of the value to compare
   * @return Empty String ""  if the type, length and value of the element staring at
   * elementStartPos is the same as the value given in in the parameter. If this was a placeholder
   * match the value of thee placeholder, null otherwise.
   */
  private @Nullable
  String compareValue(int elementStartPos, byte type, @NonNull byte[] value) {
    // Placeholder always matches
    int valuePos = elementStartPos + HEADER_LENGTH;
    boolean containsPlaceholder = false;
    int valueLength = getValueLength(elementStartPos);
    if (valueLength > 0 && byteArray[valuePos] == IDX_PLACEHOLDER) {
      valuePos += 1; // The actual value starts at +1 if we have a placeholder indicator
      valueLength -= 1; // IF placeholder the value is one byte shorter
      containsPlaceholder = true;
      if (valueLength == 2) {
        return null; // Empty placeholder id does not match
      }
      if (value.length == 0) {
        return null; // Empty string does not match the placeholder
      }
    }
    if (byteArray[elementStartPos] != type || (valueLength != value.length
        && !containsPlaceholder)) {
      return null; // Does not match
    }
    // i index over value array forward
    // j index over search byte arrays value element forward
    // k index over value array backward
    for (int i = 0; i < value.length; i++) {
      if (containsPlaceholder && byteArray[valuePos + i] == '{') {
        // Until here every char in front for the placeholder matched.
        // Now lets see of all chars behind thje placeholer also match
        for (int j = valueLength - 1, k = value.length - 1; j >= 0; j--, k--) {
          if (byteArray[valuePos + j] == '}') {
            // Text behind the placholder fully matches. Now we just need to get the placeholer
            // string and can return.
            byte[] placeholderValue = new byte[k - i + 1];
            // Size is without braces
            byte[] placeholder = new byte[(valuePos + j) - (valuePos + i) - 1];
            System.arraycopy(value, i, placeholderValue, 0, placeholderValue.length);
            System.arraycopy(byteArray, valuePos + i + 1, placeholder, 0, placeholder.length);
            return new StringBuilder(placeholderValue.length + placeholder.length + 1)
                .append(new String(placeholder)).append(MATCH_PARAM_DIVIDER_CHAR)
                .append(new String(placeholderValue)).toString();
          }
          if (byteArray[valuePos + j] != value[k]) {
            return null;
          }
        }
      }
      if (byteArray[valuePos + i] != value[i]) {
        return null; // Does not magch
      }
    }
    return ""; // Matches but is no placeholder
  }

  /**
   * Get the next entries position, or -1 if there are no further entries.
   *
   * @param elementStartPos   The start postion of the current element.
   * @param parentBoundaryPos The parent elements boundry (i.e. the first elementStartPos that is
   *                          not part of the parent element anhymore)
   * @return
   */
  private int getNextElementStartPosition(int elementStartPos, int parentBoundaryPos) {
    int nextElementPos = getElementBoundaryPos(elementStartPos);
    if (nextElementPos == parentBoundaryPos) {
      // This was the last element
      return -1;
    } else if (nextElementPos > parentBoundaryPos) {
      // TODO Remove this check for prod
      throw new IllegalStateException("Element size error at index " + elementStartPos);
    } else {
      return nextElementPos;
    }
  }

  /**
   * The elements boundary is the first elementStartPos that is not part of the parent element
   * anymore.
   *
   * @param elementStartPos
   * @return
   */
  private int getElementBoundaryPos(int elementStartPos) {
    return elementStartPos + HEADER_LENGTH + getValueLength(elementStartPos)
        + getChildrenLenght(elementStartPos);
  }

  /**
   * Get the position of the children element of the element starting at elementStartPos.
   *
   * @param elementStartPos The start position of the element to get the children for
   * @return children pos or -1 if there are no children.
   */
  private int getChildrenPos(int elementStartPos) {
    if (getChildrenLenght(elementStartPos) == 0) {
      return -1;
    } else {
      return elementStartPos + HEADER_LENGTH + getValueLength(elementStartPos);
    }
  }

  /**
   * The length of the value element of the element starting at elementStartPos.
   *
   * @param elementStartPos Starting positon of element to process
   * @return The length of the value section of this element.
   */
  private int getValueLength(int elementStartPos) {
    return readOneByteAsInt(elementStartPos + HEADER_TYPE_LENGTH);
  }

  /**
   * The length of the children section of the element starting at elementStartPos.
   *
   * @param elementStartPos Starting positon of element to process
   * @return The length of the children section of this element.
   */
  private int getChildrenLenght(int elementStartPos) {
    return readFourBytesAsInt(elementStartPos + HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT);
  }

  /**
   * @param elementStartPos
   * @return The match index for this element. It is either the match index or MAX_SHORT if no
   * match.
   */
  private int getMatchIndex(int elementStartPos) {
    return readTwoBytesAsInt(elementStartPos + HEADER_TYPE_LENGTH + HEADER_VALUE_LENGHT
        + HEADER_CHILDREN_LENGTH);
  }

  public int length() {
    return byteArray.length;
  }

  private int readOneByteAsInt(int pos) {
    return byteArray[pos] & 0xFF;
  }

  private int readTwoBytesAsInt(int pos) {
    return (byteArray[pos] & 0xFF) << 8
        | (byteArray[pos + 1] & 0xFF);
  }

  private int readFourBytesAsInt(int pos) {
    return (byteArray[pos] & 0xFF) << 24
        | (byteArray[pos + 1] & 0xFF) << 16
        | (byteArray[pos + 2] & 0xFF) << 8
        | (byteArray[pos + 3] & 0xFF);
  }

  /**
   * Get filename for match index.
   *
   * @param moduleName
   * @return
   */
  public static @NonNull
  String getMatchIdxFileName(@NonNull String moduleName) {
    return ("dld_match_" + moduleName + ".idx").toLowerCase();
  }

  public static class Match {

    private final int matchIndex;
    private final Map<String, String> parameterMap;

    public Match(int matchIndex, @NonNull Map<String, String> parameterMap) {
      this.matchIndex = matchIndex;
      this.parameterMap = parameterMap;
    }

    public int getMatchIndex() {
      return matchIndex;
    }

    public @NonNull
    Map<String, String> getParameterMap() {
      return parameterMap;
    }

  }
}
