# DeepLinkDispatch

[![Build Status](https://travis-ci.org/airbnb/DeepLinkDispatch.svg)](https://travis-ci.org/airbnb/DeepLinkDispatch)

DeepLinkDispatch provides a declarative, annotation-based API to define application deep links.

You can register an `Activity` to handle specific deep links by annotating it with `@DeepLink` and a URI.
DeepLinkDispatch will parse the URI and dispatch the deep link to the appropriate `Activity`, along
with any parameters specified in the URI.

### Example

Here's an example where we register `SampleActivity` to pull out an ID from a deep link like
`example://example.com/deepLink/123`. We annotated with `@DeepLink` and specify there will be a
parameter that we'll identify with `id`.

```java
@DeepLink("foo://example.com/deepLink/{id}")
public class MainActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Bundle parameters = intent.getExtras();
      String idString = parameters.getString("id");
      // Do something with idString
    }
  }
}
```

### Multiple Deep Links

Sometimes you'll have an Activity that handles several kinds of deep links:

```java
@DeepLink({"foo://example.com/deepLink/{id}", "foo://example.com/anotherDeepLink"})
public class MainActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Bundle parameters = intent.getExtras();
      String idString = parameters.getString("id");
      // Do something with idString
    }
  }
}
```

### Method Annotations

You can also annotate any `public static` method with `@DeepLink`. DeepLinkDispatch will call that
method to create the `Intent` and will use it when starting your `Activity` via that registered deep link:

```java
@DeepLink("foo://example.com/methodDeepLink/{param1}")
public static Intent intentForDeepLinkMethod(Context context) {
  return new Intent(context, MainActivity.class)
      .setAction(ACTION_DEEP_LINK_METHOD);
}
```

If you need access to the `Intent` extras, just add a `Bundle` parameter to your method, for example:

```java
@DeepLink("foo://example.com/methodDeepLink/{param1}")
public static Intent intentForDeepLinkMethod(Context context, Bundle extras) {
  Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
  return new Intent(context, MainActivity.class)
      .setData(uri.appendQueryParameter("bar", "baz").build())
      .setAction(ACTION_DEEP_LINK_METHOD);
}
```

If you need to customize your `Activity` backstack, you can return a `TaskStackBuilder` instead of an `Intent`. DeepLinkDispatch will call that method to create the `Intent` from the `TaskStackBuilder` last `Intent` and use it when starting your `Activity` via that registered deep link:

```java
@DeepLink("http://example.com/deepLink/{id}/{name}")
public static TaskStackBuilder intentForTaskStackBuilderMethods(Context context) {
  Intent detailsIntent =  new Intent(context, SecondActivity.class).setAction(ACTION_DEEP_LINK_COMPLEX);
  Intent parentIntent =  new Intent(context, MainActivity.class).setAction(ACTION_DEEP_LINK_COMPLEX);
  TaskStackBuilder  taskStackBuilder = TaskStackBuilder.create(context);
  taskStackBuilder.addNextIntent(parentIntent);
  taskStackBuilder.addNextIntent(detailsIntent);
  return taskStackBuilder;
}
```

### Query Parameters

Query parameters are parsed and passed along automatically, and are retrievable like any other parameter. For example, we could retrieve the query parameter passed along in the URI `foo://example.com/deepLink?qp=123`:

```java
@DeepLink("foo://example.com/deepLink")
public class MainActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Bundle parameters = intent.getExtras();
      if (parameters != null && parameters.getString("qp") != null) {
        String queryParameter = parameters.getString("qp");
        // Do something with the query parameter...
      }
    }
  }
}
```

### Callbacks

You can optionally register a `BroadcastReceiver` to be called on any incoming deep link into your
app. DeepLinkDispatch will use `LocalBroadcastManager` to broadcast an `Intent` with any success
or failure when deep linking. The intent will be populated with these extras:

* `DeepLinkHandler.EXTRA_URI`: The URI of the deep link.
* `DeepLinkHandler.EXTRA_SUCCESSFUL`: Whether the deep link was fired successfully.
* `DeepLinkHandler.EXTRA_ERROR_MESSAGE`: If there was an error, the appropriate error message.

You can register a receiver to receive this intent. An example of such a use is below:

```java
public class DeepLinkReceiver extends BroadcastReceiver {
  private static final String TAG = "DeepLinkReceiver";

  @Override public void onReceive(Context context, Intent intent) {
    String deepLinkUri = intent.getStringExtra(DeepLinkHandler.EXTRA_URI);
    if (intent.getBooleanExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, false)) {
      Log.i(TAG, "Success deep linking: " + deepLinkUri);
    } else {
      String errorMessage = intent.getStringExtra(DeepLinkHandler.EXTRA_ERROR_MESSAGE);
      Log.e(TAG, "Error deep linking: " + deepLinkUri + " with error message +" + errorMessage);
    }
  }
}

public class YourApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    IntentFilter intentFilter = new IntentFilter(DeepLinkHandler.ACTION);
    LocalBroadcastManager.getInstance(this).registerReceiver(new DeepLinkReceiver(), intentFilter);
  }
}
```

## Usage

Add to your project `build.gradle` file:

```groovy
dependencies {
  compile 'com.airbnb:deeplinkdispatch:3.0.0-rc1'
  annotationProcessor 'com.airbnb:deeplinkdispatch-processor:3.0.0-rc1'
}
```

Create your deep link module(s) (**new on DeepLinkDispatch v3**). For every class you annotate with `@DeepLinkModule`, DeepLinkDispatch will generate a "Loader" class, which contains a registry of all your `@DeepLink` annotations.

```java
/** This will generate a AppDeepLinkModuleLoader class */
@DeepLinkModule
public class AppDeepLinkModule {
}
```

**Optional**: If your Android application contains multiple modules (eg. separated Android library projects), you'll want to add one `@DeepLinkModule` class for every module in your application, so that DeepLinkDispatch can collect all your annotations in one "loader" class per module:

```java
/** This will generate a LibraryDeepLinkModuleLoader class */
@DeepLinkModule
public class LibraryDeepLinkModule {
}
```


Create any `Activity` (eg. `DeepLinkActivity`) with the scheme you'd like to handle in your `AndroidManifest.xml` file (using `foo` as an example):

```xml
<activity
    android:name="com.example.DeepLinkActivity"
    android:theme="@android:style/Theme.NoDisplay">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="foo" />
    </intent-filter>
</activity>
```

Annotate your `DeepLinkActivity` with `@DeepLinkHandler` and provide it a list of `@DeepLinkModule` annotated class(es):

```java
@DeepLinkHandler({ AppDeepLinkModule.class, LibraryDeepLinkModule.class })
public class DeepLinkActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // DeepLinkDelegate, LibraryDeepLinkModuleLoader and AppDeepLinkModuleLoader
    // are generated at compile-time.
    DeepLinkDelegate deepLinkDelegate = 
        new DeepLinkDelegate(new AppDeepLinkModuleLoader(), new LibraryDeepLinkModuleLoader());
    // Delegate the deep link handling to DeepLinkDispatch. 
    // It will start the correct Activity based on the incoming Intent URI
    deepLinkDelegate.dispatchFrom(this);
    // Finish this Activity since the correct one has been just started
    finish();
  }
}
```

### Notes

* Starting with DeepLinkDispatch v3, you have to **always** provide your own `Activity` class and annotate it with `@DeepLinkHandler`. It's no longer automatically generated by the annotation processor.
* Intent filters may only contain a single data element for a URI pattern. Create separate intent filters to capture additional URI patterns.
* Please refer to the [sample app](https://github.com/airbnb/DeepLinkDispatch/blob/master/sample/src/main/java/com/airbnb/deeplinkdispatch/sample/DeepLinkActivity.java) for an example of how to use the library.

Snapshots of the development version are available in
[Sonatype's `snapshots` repository](https://oss.sonatype.org/content/repositories/snapshots/).

## Proguard Rules

```
-keep class com.airbnb.deeplinkdispatch.** { *; }
-keepclasseswithmembers class * {
     @com.airbnb.deeplinkdispatch.DeepLink <methods>;
}
```

## Testing the sample app

Use adb to launch deep links (in the terminal type: `adb shell`).

This fires a standard deep link. Source annotation `@DeepLink("dld://example.com/deepLink")`

`am start -W -a android.intent.action.VIEW -d "dld://example.com/deepLink" com.airbnb.deeplinkdispatch.sample`

This fires a deep link associated with a method, and also passes along a path parameter. Source annotation `@DeepLink("dld://methodDeepLink/{param1}")`

`am start -W -a android.intent.action.VIEW -d "dld://methodDeepLink/abc123" com.airbnb.deeplinkdispatch.sample`

You can include multiple path parameters (also you don't have to include the sample app's package name). Source annotation `@DeepLink("http://example.com/deepLink/{id}/{name}")`

`am start -W -a android.intent.action.VIEW -d "http://example.com/deepLink/123abc/myname"`


Note it is possible to call directly `adb shell am ...` however this seems to miss the URI sometimes so better to call from within shell.

## License

```
Copyright 2015 Airbnb, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
