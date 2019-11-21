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
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DeepLinkEntry {

  private final Type type;
  /**
   * The class where the annotation corresponding to where an instance of DeepLinkEntry is declared.
   */
  private final Class<?> activityClass;
  private final String method;
  private final Pattern uriRegex;
  private final String uriTemplate;
  private final String[] parameters;

  public enum Type {
    CLASS,
    METHOD
  }

  public DeepLinkEntry(Pattern uriRegex, String uriTemplate, String[] parameters, Type type, Class<?> activityClass, String method) {
    this.uriRegex = uriRegex;
    this.uriTemplate = uriTemplate;
    this.parameters = parameters;
    this.type = type;
    this.activityClass = activityClass;
    this.method = method;
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
   * Generates a map of parameters and the values from the given deep link.
   *
   * @param inputUri the intent Uri used to launch the Activity
   * @return the map of parameter values, where all values will be strings.
   */
  public Map<String, String> getParameters(String inputUri) {
    Map<String, String> paramsMap = new HashMap<>(parameters.length);
    DeepLinkUri deepLinkUri = DeepLinkUri.parse(inputUri);
    Matcher matcher = uriRegex.matcher(new SchemeHostAndPath(deepLinkUri).getSchemeHostAndPath());
    int i = 1;
    if (matcher.matches()) {
      for (String key : parameters) {
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

  public boolean matches(SchemeHostAndPath schemeHostAndPath) {
    return uriRegex != null && uriRegex.matcher(schemeHostAndPath.getSchemeHostAndPath()).find();
  }

}
