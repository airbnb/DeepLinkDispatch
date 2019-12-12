package com.airbnb.deeplinkdispatch.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Utils {

  private static final String TAG = "Utils";

  public static byte[] readMatchIndexFromStrings(String[] strings) {
    if (strings.length == 0) {
      return new byte[]{};
    }
    if (strings.length == 1) {
      try {
        return strings[0].getBytes(MatchIndex.MATCH_INDEX_ENCODING);
      } catch (UnsupportedEncodingException e) {
        // Do nothing. Encoding is hardcoded and default.
      }
    }
    StringBuilder fullString = new StringBuilder(strings[0].length() * 2);
    for (String string : strings) {
      fullString.append(string);
    }
    try {
      return fullString.toString().getBytes(MatchIndex.MATCH_INDEX_ENCODING);
    } catch (UnsupportedEncodingException e) {
      // Do nothing. Encoding is hardcoded and default.
    }
    return null;
  }

  private static byte[] getBytes(InputStream is) throws IOException {

    int read;
    int bufferSize = 8192; // Default buffer size of underlying impelmentation
    byte[] buffer = new byte[bufferSize];

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    while ((read = is.read(buffer, 0, bufferSize)) != -1) {
      bos.write(buffer, 0, read);
    }

    return bos.toByteArray();
  }

}
