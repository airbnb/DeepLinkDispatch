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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public final class DeepLinkEntry {
  private static final String PARAM_VALUE = "([a-zA-Z0-9_#'!+%~,\\-\\.\\@\\$\\:]+)";
  private static final String PARAM = "([a-zA-Z][a-zA-Z0-9_-]*)";
  private static final String PARAM_REGEX = "%7B(" + PARAM + ")%7D";
  private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX);

  private final Type type;
  private final Class<?> activityClass;
  private final String method;
  private final Set<String> parameters;
  private final List<Pattern> regexes;

  public enum Type {
    CLASS,
    METHOD
  }

  public DeepLinkEntry(String uri, Type type, Class<?> activityClass, String method,
                       @Nullable String[] prefixes) {
    DeepLinkUri parsedUri;
    String uriString;
    if (prefixes == null) {
      parsedUri = DeepLinkUri.parse(uri);
      uriString = schemeHostAndPath(parsedUri);
    } else {
      parsedUri = DeepLinkUri.parse(prefixes[0] + uri);
      uriString = hostAndPath(parsedUri);
    }
    this.type = type;
    this.activityClass = activityClass;
    this.method = method;
    this.parameters = parseParameters(parsedUri);
    String replacedUriString = uriString.replaceAll(PARAM_REGEX, PARAM_VALUE);
    this.regexes = prefixes == null ? Collections.singletonList(Pattern.compile(replacedUriString))
            : createRegexForPrefixes(replacedUriString + "$", prefixes);
  }

  private List<Pattern> createRegexForPrefixes(String uri, String[] prefixes) {
    List<Pattern> regexes = new ArrayList<>();

    for (String prefix : prefixes) {
      regexes.add(Pattern.compile(prefix + uri));
    }

    return regexes;
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
    String schemeHostAndPath = schemeHostAndPath(deepLinkUri);
    for (Pattern regex : regexes) {
      Matcher matcher = regex.matcher(schemeHostAndPath);
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
    }
    return paramsMap;
  }

  private static String parsePath(DeepLinkUri parsedUri) {
    return parsedUri.encodedPath();
  }

  public boolean matches(String inputUri) {
    DeepLinkUri deepLinkUri = DeepLinkUri.parse(inputUri);
    if (deepLinkUri == null) {
      return false;
    }
    for (Pattern regex : regexes) {
      if (regex.matcher(schemeHostAndPath(deepLinkUri)).find()) {
        return true;
      }
    }
    return false;
  }

  private String schemeHostAndPath(DeepLinkUri uri) {
    return uri.scheme() + "://" + uri.encodedHost() + parsePath(uri);
  }

  private String hostAndPath(DeepLinkUri uri) {
    return uri.encodedHost() + parsePath(uri);
  }
}
