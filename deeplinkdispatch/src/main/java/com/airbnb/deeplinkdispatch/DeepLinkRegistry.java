package com.airbnb.deeplinkdispatch;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepLinkRegistry {

  private List<DeepLinkEntry> registry = new LinkedList<>();

  public DeepLinkRegistry(Loader loader) {
    if (loader != null) {
      loader.load(this);
    }
  }

  /**
   * Registers the deep link that DeepLinkActivity will handle, along with where to delegate
   *
   * @param hostPath the combined host and path of the deep link
   * @param type     whether its a class level annotation or method level
   * @param activity the activity to delegate the deep link to
   * @param method   the method used to generate the <code>Intent</code>
   */
  public void registerDeepLink(String hostPath, DeepLinkEntry.Type type, String activity,
      String method) {
    registry.add(new DeepLinkEntry(hostPath, type, activity, method));
  }

  /**
   * Search through the registry to see if there is a matching entry for the given deep link.
   *
   * @param hostPath the combined host and path of the deep link
   * @return the entry containing the information of which activity and method handles the deep link
   */
  public DeepLinkEntry parseUri(String hostPath) {
    for (DeepLinkEntry entry : registry) {
      Matcher matcher = Pattern.compile(entry.getRegex()).matcher(hostPath);
      if (matcher.matches()) {
        return entry;
      }
    }

    return null;
  }
}
