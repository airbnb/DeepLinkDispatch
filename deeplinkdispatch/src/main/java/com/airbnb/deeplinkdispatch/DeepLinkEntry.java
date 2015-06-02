package com.airbnb.deeplinkdispatch;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepLinkEntry {

  private static final String PARAM_VALUE = "([a-zA-Z0-9]*)";
  private static final String PARAM = "([a-zA-Z][a-zA-Z0-9_-]*)";
  private static final String PARAM_REGEX = "\\{(" + PARAM + ")\\}";

  private final String hostPath;
  private final String regex;
  private final Type type;
  private final String activity;
  private final String method;

  public enum Type {
    CLASS,
    METHOD
  }

  public DeepLinkEntry(String hostPath, Type type, String activity, String method) {
    this.hostPath = hostPath;
    this.regex = generateLookupString(hostPath);
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

  /**
   * Generates a map of parameters and the values from the given deep link.
   * @param inputHostPath the combined host and path of the deep link
   * @return the map of parameter values, where all values will be strings.
   */
  public Map<String, String> getParameters(String inputHostPath) {
    Map<String, String> parameters = generateParameterMap(hostPath);
    populateParameters(inputHostPath, parameters);
    return parameters;
  }

  private Map<String, String> generateParameterMap(String hostPath) {
    Map<String, String> paramMap = new LinkedHashMap<>();
    Pattern pattern = Pattern.compile(PARAM_REGEX);
    Matcher matcher = pattern.matcher(hostPath);
    while (matcher.find()) {
      paramMap.put(matcher.group(1), "");
    }
    return paramMap;
  }

  private void populateParameters(String inputHostPath, Map<String, String> parameters) {
    Iterator<String> keySetIterator = parameters.keySet().iterator();
    Matcher matcher = Pattern.compile(getRegex()).matcher(inputHostPath);
    matcher.matches();

    int i = 1;
    while (keySetIterator.hasNext()) {
      String key = keySetIterator.next();
      parameters.put(key, matcher.group(i));
      i++;
    }
  }

  private String generateLookupString(String hostPath) {
    return hostPath.replaceAll(PARAM_REGEX, PARAM_VALUE);
  }
}
