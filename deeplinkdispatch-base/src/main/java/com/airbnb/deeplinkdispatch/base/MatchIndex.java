package com.airbnb.deeplinkdispatch.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.deeplinkdispatch.NodeMetadata;
import com.airbnb.deeplinkdispatch.UrlElement;
import com.airbnb.deeplinkdispatch.UrlTreeKt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a wrapper class around the byte array match index.
 * <p>Byte array format is:</p>
 * <hr/>
 * <table border="1">
 * <tr>
 * <td>Node's metadata flags</td>
 * <td>value length</td>
 * <td>children length</td><td>match id</td><td>value</td><td>children</td>
 * </tr>
 * <tr>
 * <td>1 byte</td><td>1 byte</td><td>4 bytes</td><td>2 bytes</td>
 * <td>9 + value length bytes</td><td>9 + value length + children length bytes</td>
 * </tr>
 * <tr>
 * <td>{@linkplain com.airbnb.deeplinkdispatch.NodeMetadata}
 * e.g. scheme, authority, path segment, transformation</td><td>length of the
 * node's (string) value, in bytes</td>
 * </tr>
 * </table>
 * <hr/>
 * <p>
 * This is implemented in Java for speed reasons. Converting this class to Kotlin made the
 * whole lookup operation multiple times slower.
 * This is most likely not a Kotlin issue but some syntactic sugar used must have crated some
 * overhead.
 * As this is very "bare metal" anyway it was "safer" to write this in Java to avoid any
 * instructions to be added that are not necessary.</p>
 */
public class MatchIndex {

  /**
   * Encoding used for serialization
   */
  @NonNull
  public static final String MATCH_INDEX_ENCODING = "ISO_8859_1";

  /**
   * Length of header elements in bytes
   */
  private static final int HEADER_NODE_METADATA_LENGTH = 1;
  private static final int HEADER_VALUE_LENGTH = 1;
  private static final int HEADER_CHILDREN_LENGTH = 4;
  private static final int HEADER_MATCH_ID_LENGTH = 2;

  public static final int HEADER_LENGTH = HEADER_NODE_METADATA_LENGTH + HEADER_VALUE_LENGTH
    + HEADER_CHILDREN_LENGTH + HEADER_MATCH_ID_LENGTH;

  /**
   * Marker for no match
   */
  public static final int NO_MATCH = 0xffff;

  @NonNull
  public static final String ROOT_VALUE = "r";

  // Used to separate param and param value in compare return value (record separator)
  @NonNull
  public static final String MATCH_PARAM_DIVIDER_CHAR = String.valueOf((char) 0x1e);

  @NonNull
  private byte[] byteArray;

  public MatchIndex(@NonNull byte[] byteArray) {
    this.byteArray = byteArray;
  }

  public Match matchUri(@NonNull List<UrlElement> elements, @Nullable Map<String, String>
    placeholders, int elementIndex, int elementStartPosition, int parentBoundryPos,
                        Map<String, String> pathSegmentReplacements) {
    Match match = null;
    int currentElementStartPosition = elementStartPosition;
    do {
      UrlElement urlElement = elements.get(elementIndex);
      String compareResult = compareValue(currentElementStartPosition, urlElement.getTypeFlag(),
        urlElement.getValue(), pathSegmentReplacements);
      if (compareResult != null) {
        Map<String, String> placeholdersOutput = placeholders;
        // If the compareResult is not empty we found a match with a placeholder. We need to save
        // that placeholder -- and the value it was placeholding for -- in a map and possibly
        // hand it down to the next level of recursion.
        if (!compareResult.isEmpty()) {
          // We need to have a new HashMap for every aprtial match to make sure that the
          // placeholders found in other partial matches do not overlap with the actual final match.
          placeholdersOutput = new HashMap<>(placeholders != null ? placeholders
            : Collections.<String, String>emptyMap());
          String[] compareParams = compareResult.split(MATCH_PARAM_DIVIDER_CHAR);
          // Add the found placeholder set to the map.
          placeholdersOutput.put(compareParams[0], compareParams[1]);
        }
        if (elementIndex < elements.size() - 1) {
          // If value matched we need to explore this elements children next.
          int childrenPos = getChildrenPos(currentElementStartPosition);
          if (childrenPos != -1) {
            match = matchUri(elements, placeholdersOutput, elementIndex + 1,
              childrenPos, getElementBoundaryPos(currentElementStartPosition),
              pathSegmentReplacements);
          }
        } else {
          int matchIndex = getMatchIndex(currentElementStartPosition);
          // Url is a partial match.
          if (matchIndex == NO_MATCH) {
            return null;
          }
          match = new Match(matchIndex, placeholdersOutput == null
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
   * @param elementStartPos         The start position of the element to compare
   * @param inboundUriComponentType A flag for the URI component type of the inboundValue to
   *                                compare (like scheme, host, or path segment)
   * @param inboundValue            The byte array of the inbound URI
   * @return Empty String ""  if the type, length and inboundValue of the element staring at
   * elementStartPos is the same as the inboundValue given in in the parameter. If this was a
   * placeholder match the inboundValue of thee placeholder, null otherwise.
   */
  @Nullable
  private String compareValue(int elementStartPos, byte inboundUriComponentType, @NonNull byte[]
    inboundValue, Map<String, String> pathSegmentReplacements) {
    // Placeholder always matches
    int valueStartPos = elementStartPos + HEADER_LENGTH;
    NodeMetadata nodeMetadata = new NodeMetadata(byteArray[elementStartPos]);

    //Opportunistically skip doing more operations on this node if we can infer it will not match
    //based on URIComponentType
    if (nodeMetadata.isComponentTypeMismatch(inboundUriComponentType)) return null;

    int valueLength = getValueLength(elementStartPos);
    boolean isValueLengthMismatch = valueLength != inboundValue.length;
    if (isValueLengthMismatch && nodeMetadata.isValueLiteralValue) {
      //Opportunistically skip this node if the comparator's lengths mismatch.
      return null;
    }

    if (nodeMetadata.isComponentParam) {
      return compareComponentParam(valueStartPos, valueLength, inboundValue);
    } else if (nodeMetadata.isConfigurablePathSegment) {
      return compareConfigurablePathSegment(inboundValue, pathSegmentReplacements, valueStartPos,
        valueLength);
    } else {
      return compareValuesWalk(inboundValue, valueStartPos, valueLength);
    }
  }

  private String compareValuesWalk(byte[] inboundValue, int valueStartPos, int valueLength) {
    for (int i = 0; i < valueLength; i++) {
      if (inboundValue[i] != byteArray[valueStartPos + i]) return null;
    }
    return "";
  }

  @Nullable
  private String compareConfigurablePathSegment(@NonNull byte[] inboundValue,
                                                Map<String, String> pathSegmentReplacements,
                                                int valueStartPos, int valueLength) {
    // Copy a chunk of values from byteArray to use as a key for looking up path segment from
    // pathSegmentReplacements.
    byte[] byteArrayValue = new byte[valueLength];
    System.arraycopy(byteArray, valueStartPos, byteArrayValue, 0, valueLength);
    String pathSegmentKey = new String(byteArrayValue).substring(
      UrlTreeKt.configurablePathSegmentPrefix.length(),
      valueLength - UrlTreeKt.configurablePathSegmentSuffix.length()
    );

    String replacementValue = pathSegmentReplacements.get(pathSegmentKey);
    if (new String(inboundValue).equals(replacementValue)) {
      return "";
    } else {
      return null;
    }
  }

  @Nullable
  private String compareComponentParam(
    int valueStartPos, int valueLength, @NonNull byte[] inboundValue
  ) {
    if ((
      //Per com.airbnb.deeplinkdispatch.DeepLinkEntryTest.testEmptyParametersNameDontMatch
      //We should not return a match if the param (aka placeholder) is empty.
      byteArray[valueStartPos] == '{' && byteArray[valueStartPos + 1] == '}'
    ) || (
      //Per com.airbnb.deeplinkdispatch.DeepLinkEntryTest.testEmptyPathPresentParams
      //We expect an empty path to not be a match
      inboundValue.length == 0
    )) {
      return null;
    }
    // i index over inboundValue array forward
    // j index over search byte arrays inboundValue element forward
    // k index over inboundValue array backward
    for (int i = 0; i < inboundValue.length; i++) {
      if (byteArray[valueStartPos + i] == '{') {
        // Until here every char in front for the placeholder matched.
        // Now let's see if all chars within the placeholder also match.
        for (int j = valueLength - 1, k = inboundValue.length - 1; j >= 0; j--, k--) {
          if (byteArray[valueStartPos + j] == '}') {
            // Text within the placeholder fully matches. Now we just need to get the placeholder
            // string and can return.
            byte[] placeholderValue = new byte[k - i + 1];
            // Size is without braces
            byte[] placeholder = new byte[(valueStartPos + j) - (valueStartPos + i) - 1];
            System.arraycopy(inboundValue, i, placeholderValue, 0, placeholderValue.length);
            System.arraycopy(byteArray, valueStartPos + i + 1, placeholder, 0,
              placeholder.length);
            return new StringBuilder(placeholderValue.length + placeholder.length + 1)
              .append(new String(placeholder)).append(MATCH_PARAM_DIVIDER_CHAR)
              .append(new String(placeholderValue)).toString();
          }
          if (byteArray[valueStartPos + j] != inboundValue[k]) {
            return null;
          }
        }
      }
      if (byteArray[valueStartPos + i] != inboundValue[i]) {
        return null; // Does not match
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
      + getChildrenLength(elementStartPos);
  }

  /**
   * Get the position of the children element of the element starting at elementStartPos.
   *
   * @param elementStartPos The start position of the element to get the children for
   * @return children pos or -1 if there are no children.
   */
  private int getChildrenPos(int elementStartPos) {
    if (getChildrenLength(elementStartPos) == 0) {
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
    return readOneByteAsInt(
      elementStartPos
        + HEADER_NODE_METADATA_LENGTH
    );
  }

  /**
   * The length of the children section of the element starting at elementStartPos.
   *
   * @param elementStartPos Starting positon of element to process
   * @return The length of the children section of this element.
   */
  private int getChildrenLength(int elementStartPos) {
    return readFourBytesAsInt(
      elementStartPos
        + HEADER_NODE_METADATA_LENGTH
        + HEADER_VALUE_LENGTH
    );
  }

  /**
   * @param elementStartPos
   * @return The match index for this element. It is either the match index or MAX_SHORT if no
   * match.
   */
  private int getMatchIndex(int elementStartPos) {
    return readTwoBytesAsInt(
      elementStartPos
        + HEADER_NODE_METADATA_LENGTH
        + HEADER_VALUE_LENGTH
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
   * @param moduleName The module name the match index is for.
   * @return The filename used to store the match index.
   */
  public static @NonNull
  String getMatchIdxFileName(@NonNull String moduleName) {
    return "dld_match_" + moduleName.toLowerCase() + ".idx";
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
