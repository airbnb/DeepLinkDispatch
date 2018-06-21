package com.airbnb.deeplinkdispatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates to DeepLinkDispatch that a {@code Loader} class should be generated.
 * Loader classes collect all the {@link DeepLink} annotations from your code and keep them in a
 * registry. This registry is then used by {@code DeepLinkDelegate} in order to decide which
 * Activity will receive each incoming deep link.
 * For example, if you annotated a class {@code FooBar} with {@link DeepLinkModule}, then
 * DeepLinkDispatch will generate a class called {@code FooBarLoader}. Also, {@code FooBar} will be
 * added as a constructor argument to the {@code DeepLinkDelegate} class, so it can be used for
 * Intent delivery.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface DeepLinkModule {
}
