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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DeepLinkEntry {

  private static final String PARAM_VALUE = "([a-zA-Z0-9_-]*)";
  private static final String PARAM = "([a-zA-Z][a-zA-Z0-9_-]*)";
  private static final String PARAM_REGEX = "\\{(" + PARAM + ")\\}";

  private final Type type;
  private final Class<?> activityClass;
  private final String method;
  private final Set<String> parameters;
  private final String regex;

  enum Type {
    CLASS,
    METHOD
  }

  DeepLinkEntry(String uri, Type type, Class<?> activityClass, String method) {
    this.type = type;
    this.activityClass = activityClass;
    this.method = method;
    this.parameters = parsePathParameters(uri);
    this.regex = schemeHostAndPath(uri).replaceAll(PARAM_REGEX, PARAM_VALUE);
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
  private static Set<String> parsePathParameters(String path) {
    Matcher m = Pattern.compile(PARAM_REGEX).matcher(path);
    Set<String> patterns = new LinkedHashSet<>();
    while (m.find()) {
      patterns.add(m.group(1));
    }
    return patterns;
  }

  /**
   * Generates a map of parameters and the values from the given deep link.
   *
   * @param inputUri the intent Uri used to launch the Activity
   * @return the map of parameter values, where all values will be strings.
   */
  Map<String, String> getParameters(String inputUri) {
    Iterator<String> paramsIterator = parameters.iterator();
    Map<String, String> paramsMap = new HashMap<>(parameters.size());
    Matcher matcher = Pattern.compile(regex).matcher(schemeHostAndPath(inputUri));
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
    return paramsMap;
  }

  private static String parsePath(DeepLinkUri parsedUri) {
    try {
      return URLDecoder.decode(parsedUri.encodedPath(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return "";
    }
  }

  boolean matches(String inputUri) {
    return Pattern.compile(regex).matcher(schemeHostAndPath(inputUri)).find();
  }

  private String schemeHostAndPath(String uri) {
    DeepLinkUri parsedUri = DeepLinkUri.parse(uri);
    return parsedUri.scheme() + "://" + parsedUri.host() + parsePath(parsedUri);
  }
}
