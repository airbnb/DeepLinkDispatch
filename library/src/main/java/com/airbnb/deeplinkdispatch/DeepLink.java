package com.airbnb.deeplinkdispatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Associate a method to handle a deeplink URI.
 * <pre><code>
 * {@literal @}DeepLink(uri);
 * </code></pre>
 */
@Retention(RetentionPolicy.CLASS) @Target({ElementType.TYPE, ElementType.METHOD})
public @interface DeepLink {
  /** The deeplink URI the method will handle */
  String uri();
}
