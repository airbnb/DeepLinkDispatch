package com.airbnb.deeplinkdispatch;

public final class ProcessorUtils {

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

  /**
   * Constrain the allowable characters for a path segment placeholder replacement.
   */
  static boolean containsUnsafe(String s) {
    return !s.matches("[a-zA-Z0-9/-]*");
  }
}
