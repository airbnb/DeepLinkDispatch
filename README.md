# DeepLinkDispatch

`DeepLinkDispatch` is a library that lets you declare you deep links programmatically. 

You can register an `Activity` to handle specific deep links by annotating with a URI. DeepLinkDispatch will parse the URI and dispatch the deep link to the appropriate `Activity`, along with any parameters specified by the URI.

### Example

Here's an example where we register `SampleActivity` to pull out an ID from a deep link like `example://example.com/deepLink/123`. We annotated with `@DeepLink` and specify there will be a parameter that we'll identify with `id`.

```
@DeepLink("example.com/deepLink/{id}")
public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Bundle parameters = getIntent().getExtras();
    
      String idString = parameters.getString("id");
    
      // Do something with the ID...
    }
    ...
  }
}
```

### Multiple Deep Links

Sometimes you'll have an activity that should handle several kinds of deep links. You can use the `@DeepLinks` annotation to register multiple deep links on an activity:

```
@DeepLinks({"example.com/deepLink/{id}", "example.com/anotherDeepLink"})
public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Bundle parameters = getIntent().getExtras();
    
      String idString = parameters.getString("id");
    
      // Do something with the ID...
    }
    ...
  }
}
```

### Method Annotations

You can also annotate static methods that return an `Intent`. `DeepLinkDispatch` will call that method to create that `Intent` and use it when starting your activity via that registered deep link:

```
  @DeepLink("example.com/methodDeepLink/{param1}")
  public static Intent intentForDeepLinkMethod(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.setAction(ACTION_DEEP_LINK_METHOD);
    return intent;
  }
```

### Query Parameters

Query parameters are parsed and passed along automatically, along with retrievable like it was any other parameters. For example, we could retrieve the query parameter passed along in the URI `example://example.com/deepLink?qp=123`:

```
@DeepLink("example.com/deepLink")
public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Bundle parameters = getIntent().getExtras();
    
      if (parameters != null && parameters.getString("qp") != null) {
        String queryParameter = parameters.getString("qp");
        
        // Do something with the query parameter...
      }
    }
  }
}
```

### Callbacks

You can optionally register callbacks to be called on any deep link success or failure. Simply implement `DeepLinkCallback` on your `Application`, and `DeepLinkDispatch` will call them appropriately:

```
public class SampleApplication extends Application implements DeepLinkCallback {

  private static final String TAG = "DeepLinkDispatch";

  @Override
  public void onSuccess(String uri) {
    Log.i(TAG, "Successful deep link: " + uri.toString());
  }

  @Override
  public void onError(DeepLinkError error) {
    Log.e(TAG, "Deep Link Error: " + error.getErrorMessage());
  }
}
```

## Including DeepLinkDispatch

It's straight-foward to add `DeepLinkDispatch`:

Modify your `build.gradle` file to include the library by adding the line:

```
compile 'com.airbnb:deeplinkdispatch:1.1.0'
apt 'com.airbnb:deeplinkdispatch-processor:1.1.0'
```

Register `DeepLinkActivity` with the scheme you'd like in your `AndroidManifest.xml` file (using `airbnb` as an example):

```
<activity
    android:name="com.airbnb.deeplinkdispatch.DeepLinkActivity"
    android:theme="@android:style/Theme.NoDisplay">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="airbnb" />
    </intent-filter>
</activity>
```

That's it. The library will generate `DeepLinkActivity` during compilation.

## Testing the sample

Use adb to launch deep links.

This fires a standard deep link:

`adb shell am start -W -a android.intent.action.VIEW -d "airbnb://example.com/deepLink" com.airbnb.deeplinkdispatch.sample`

This fires a deep link associated with a method, and also passes along a parameter:

`adb shell am start -W -a android.intent.action.VIEW -d "airbnb://methodDeepLink/abc123" com.airbnb.deeplinkdispatch.sample`

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
