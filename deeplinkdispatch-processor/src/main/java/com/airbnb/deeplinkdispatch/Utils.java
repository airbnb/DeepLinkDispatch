package com.airbnb.deeplinkdispatch;

final class Utils {
  private static final String PARAM = "@param";
  private static final String RETURN = "@return";

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

  /** Strips off {@link #PARAM} and {@link #RETURN}, and removes line separator. */
  static String formatJavaDoc(String str) {
    if (str != null) {
      str = str.trim().replace(System.lineSeparator(), " ");
      int paramPos = str.indexOf(PARAM);
      if (paramPos != -1) {
        str = str.substring(0, paramPos);
      }
      int returnPos = str.indexOf(RETURN);
      if (returnPos != -1) {
        str = str.substring(0, returnPos);
      }
      str = str.trim();
    }
    return str;
  }
}
