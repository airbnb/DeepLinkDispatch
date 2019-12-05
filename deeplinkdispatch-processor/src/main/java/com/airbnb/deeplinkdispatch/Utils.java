package com.airbnb.deeplinkdispatch;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Utils {

  private static final String PARAM = "([a-zA-Z][a-zA-Z0-9_-]*)";
  private static final String PARAM_REGEX = "%7B(" + PARAM + ")%7D";
  private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX);

  /**
   * Gets the set of unique path parameters used in the given URI. If a parameter is used twice
   * in the URI, it will only show up once in the set.
   *
   * The output is in JavaCode as a String[] initializer. e.g. "new String[] {"parm1"}"
   *
   * @param uri The DeepLinkUri to get the parameter set for.
   * @return A string representation of an array initializer for the given set of elements
   */
  public static String parseParametersIntoArrayString(DeepLinkUri uri) {
    Matcher matcher = PARAM_PATTERN.matcher(uri.encodedHost() + uri.encodedPath());
    List<String> list = new LinkedList<>();
    while (matcher.find()) {
      list.add("\""+matcher.group(1)+"\"");
    }
    return "new String[] {"+String.join(", ",list)+"}";
  }

  static String decapitalize(String str) {
    if (str != null && str.length() != 0) {
      if (str.length() > 1 && Character.isUpperCase(str.charAt(1))
          && Character.isUpperCase(str.charAt(0))) {
        return str;
      } else {
        char[] var1 = str.toCharArray();
        var1[0] = Character.toLowerCase(var1[0]);
        return new String(var1);
      }
    } else {
      return str;
    }
  }

  static boolean hasEmptyOrNullString(String[] strings) {
    for (String s : strings) {
      if (s == null || s.isEmpty()) {
        return true;
      }
    }
    return false;
  }
}
