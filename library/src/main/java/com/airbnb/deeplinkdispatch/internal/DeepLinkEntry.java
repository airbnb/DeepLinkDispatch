package com.airbnb.deeplinkdispatch.internal;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepLinkEntry {

  private static final String PARAM_VALUE = "([a-zA-Z0-9]*)";
  private static final String PARAM = "([a-zA-Z][a-zA-Z0-9_-]*)";
  private static final String PARAM_REGEX = "\\{(" + PARAM + ")\\}";

  public enum Type {
    CLASS,
    METHOD
  }

  private String uri;
  private String regex;
  private Type type;
  private String activity;
  private String method;

  public DeepLinkEntry(String uri, Type type, String activity, String method) {
    this.uri = uri;
    this.regex = generateLookupString(uri);
    this.type = type;
    this.activity = activity;
    this.method = method;
  }

  public String getRegex() {
    return regex;
  }

  public Type getType() {
    return type;
  }

  public String getActivity() {
    return activity;
  }

  public String getMethod() {
    return method;
  }

  public Map<String, String> getParameters(String inputUri) {
    Map<String, String> parameters = generateParameterMap(uri);
    populateParameters(inputUri, parameters);
    return parameters;
  }

  private Map<String, String> generateParameterMap(String uri) {
    Map<String, String> paramMap = new LinkedHashMap<>();
    Pattern pattern = Pattern.compile(PARAM_REGEX);
    Matcher matcher = pattern.matcher(uri);
    while (matcher.find()) {
      paramMap.put(matcher.group(1), "");
    }
    return paramMap;
  }

  private void populateParameters(String inputUri, Map<String, String> parameters) {
    Iterator<String> keySetIterator = parameters.keySet().iterator();
    Matcher matcher = Pattern.compile(getRegex()).matcher(inputUri);
    matcher.matches();

    int i = 1;
    while (keySetIterator.hasNext()) {
      String key = keySetIterator.next();
      parameters.put(key, matcher.group(i));
      i++;
    }
  }

  private String generateLookupString(String uri) {
    return uri.replaceAll(PARAM_REGEX, PARAM_VALUE);
  }
}
