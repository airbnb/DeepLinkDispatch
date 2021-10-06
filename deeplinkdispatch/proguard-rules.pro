# Keep the deep link handler classes (and INSTANCE field if they have it) as they are accessed
# via reflection from within the app.
-keep class * extends com.airbnb.deeplinkdispatch.handler.DeepLinkHandler {
    public static final ** INSTANCE;
}

# We need to keep the constructors of the argument objects used in the handleDeepLink methods.
# As we instantiate those constructors at runtime and we rely on the full constructor to
# be present, as it has to match the values available in the URL tem
-if class * extends com.airbnb.deeplinkdispatch.handler.DeepLinkHandler {
    public void handleDeepLink(android.content.Context, **);
}
-keep,allowobfuscation class <2> {
    <init>(...);
}

# We read annotation values of this annotation at runtime so we need to keep it around.
-keep @interface com.airbnb.deeplinkdispatch.handler.DeeplinkParam

# Keep the actual deep link annotation. We read the annotation value during runtime and thus
# this needs to be kept
-keep @interface com.airbnb.deeplinkdispatch.DeepLink
# Keep methods annotated with the DeepLink annotation. These methods are called via reflection
# at runtime and thus need to be kept.
-keepclasseswithmembers class * {
    @com.airbnb.deeplinkdispatch.DeepLink <methods>;
}
