package com.airbnb.deeplinkdispatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Associate a method to handle a deeplink URI.
 * <pre><code>
 * {@literal @}DeepLink(host, path);
 * </code></pre>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface DeepLink {
  String IS_DEEP_LINK = "is_deep_link_flag";

  String host();
  String path() default "";
}
