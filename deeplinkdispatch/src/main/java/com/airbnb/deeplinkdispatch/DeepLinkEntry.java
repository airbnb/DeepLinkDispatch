/*
 * Copyright (C) 2015 Airbnb, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.airbnb.deeplinkdispatch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DeepLinkEntry {
  private static final String PARAM_VALUE = "([a-zA-Z0-9_#'!+%~,\\-\\.\\$]*)";
  private static final String PARAM = "([a-zA-Z][a-zA-Z0-9_-]*)";
  private static final String PARAM_REGEX = "%7B(" + PARAM + ")%7D";

  private final Type type;
  private final Class<?> activityClass;
  private final String method;
  private final String uri;

  enum Type {
    CLASS,
    METHOD
  }

  DeepLinkEntry(String uri, Type type, Class<?> activityClass, String method) {
    this.type = type;
    this.activityClass = activityClass;
    this.method = method;
    this.uri = uri;
  }

  public Type getType() {
    return type;
  }

  public Class<?> getActivityClass() {
    return activityClass;
  }

  public String getMethod() {
    return method;
  }

  /**
   * Gets the set of unique path parameters used in the given URI. If a parameter is used twice
   * in the URI, it will only show up once in the set.
   */
  private static Set<String> parseParameters(DeepLinkUri uri) {
    Matcher matcher = Pattern.compile(PARAM_REGEX).matcher(uri.encodedHost() + uri.encodedPath());
    Set<String> patterns = new LinkedHashSet<>();
    while (matcher.find()) {
      patterns.add(matcher.group(1));
    }
    return patterns;
  }

  private static String parsePath(DeepLinkUri parsedUri) {
    return parsedUri.encodedPath();
  }

  Optional<DeepLinkMatch> matches(String inputUri) {
    DeepLinkUri parsedUri = DeepLinkUri.parse(uri);
    String schemeHostAndPath = schemeHostAndPath(parsedUri);
    Set<String> parameters = parseParameters(parsedUri);
    Pattern regex = Pattern.compile(schemeHostAndPath.replaceAll(PARAM_REGEX, PARAM_VALUE) + "$");

    DeepLinkUri inputParsedUri = DeepLinkUri.parse(inputUri);
    boolean isMatch = inputParsedUri != null && regex.matcher(schemeHostAndPath(inputParsedUri)).find();

    if (isMatch) {
      Iterator<String> paramsIterator = parameters.iterator();
      Map<String, String> paramsMap = new HashMap<>(parameters.size());
      Matcher matcher = regex.matcher(schemeHostAndPath(inputParsedUri));
      int i = 1;
      if (matcher.matches()) {
        while (paramsIterator.hasNext()) {
          String key = paramsIterator.next();
          String value = matcher.group(i++);
          if (value != null && !"".equals(value.trim())) {
            paramsMap.put(key, value);
          }
        }
      }
      return Optional.of(new DeepLinkMatch(this, paramsMap, inputParsedUri));

    } else {
      return Optional.absent();
    }
  }

  private String schemeHostAndPath(DeepLinkUri uri) {
    return uri.scheme() + "://" + uri.encodedHost() + parsePath(uri);
  }
}
