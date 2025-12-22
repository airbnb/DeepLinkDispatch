# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/christian/Environment/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Custom deep link annotations (AppDeepLink, WebDeepLink, WebPlaceholderDeepLink) are now
# automatically kept by the consumer proguard rules in deeplinkdispatch module.
# Any annotation marked with @DeepLinkSpec is automatically kept.

# Need to keep as we use reflection to read this field to get the type.
-keepclassmembers class com.airbnb.deeplinkdispatch.sample.typeconversion.TypeConversionErrorHandlerCustomTypeDeepLinkActivity {
    java.util.List stringList;
}