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

public final class DeepLinkEntry {
  private static final String PARAM_VALUE = "([a-zA-Z0-9_#'!+%~,\\-\\.\\@\\$\\:]+)";
  private static final String PARAM = "([a-zA-Z][a-zA-Z0-9_-]*)";
  private static final String PARAM_REGEX = "%7B(" + PARAM + ")%7D";
  private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX);

  private final Type type;
  private final Class<?> activityClass;
  private final String method;
  private final Set<String> parameters;
  private final Pattern regex;
  private final String uriTemplate;

  public enum Type {
    CLASS,
    METHOD
  }

  public DeepLinkEntry(String uriTemplate, Type type, Class<?> activityClass, String method) {
    DeepLinkUri parsedUri = DeepLinkUri.parse(uriTemplate);
    String schemeHostAndPath = schemeHostAndPath(parsedUri);

    this.uriTemplate = uriTemplate;
    this.type = type;
    this.activityClass = activityClass;
    this.method = method;
    this.parameters = parseParameters(parsedUri);
    this.regex = Pattern.compile(schemeHostAndPath.replaceAll(PARAM_REGEX, PARAM_VALUE) + "$");
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
    Matcher matcher = PARAM_PATTERN.matcher(uri.encodedHost() + uri.encodedPath());
    Set<String> patterns = new LinkedHashSet<>();
    while (matcher.find()) {
      patterns.add(matcher.group(1));
    }
    return patterns;
  }

  /**
   * Generates a map of parameters and the values from the given deep link.
   *
   * @param inputUri the intent Uri used to launch the Activity
   * @return the map of parameter values, where all values will be strings.
   */
  public Map<String, String> getParameters(String inputUri) {
    Iterator<String> paramsIterator = parameters.iterator();
    Map<String, String> paramsMap = new HashMap<>(parameters.size());
    DeepLinkUri deepLinkUri = DeepLinkUri.parse(inputUri);
    Matcher matcher = regex.matcher(schemeHostAndPath(deepLinkUri));
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

  public String getUriTemplate() {
    return this.uriTemplate;
  }

  private static String parsePath(DeepLinkUri parsedUri) {
    return parsedUri.encodedPath();
  }

  public boolean matches(String inputUri) {
    DeepLinkUri deepLinkUri = DeepLinkUri.parse(inputUri);
    return deepLinkUri != null && regex.matcher(schemeHostAndPath(deepLinkUri)).find();
  }

  private String schemeHostAndPath(DeepLinkUri uri) {
    return uri.scheme() + "://" + uri.encodedHost() + parsePath(uri);
  }
}
