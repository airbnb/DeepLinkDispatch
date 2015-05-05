package com.airbnb.deeplinkdispatch.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.airbnb.deeplinkdispatch.DeepLinkEntry;
import com.airbnb.deeplinkdispatch.DeepLinkLoader;
import com.airbnb.deeplinkdispatch.DeepLinkRegistry;
import com.airbnb.deeplinkdispatch.Loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class DeepLinkActivity extends Activity {

  private static final String TAG = DeepLinkActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Loader loader = new DeepLinkLoader();

    DeepLinkRegistry registry = new DeepLinkRegistry(loader);

    Uri uri = getIntent().getData();
    DeepLinkEntry entry = registry.parseUri(uri.toString());

    if (entry != null) {
      Map<String, String> parameterMap = entry.getParameters(uri.toString());

      try {
        Class<?> c = Class.forName(entry.getActivity());

        Intent intent;
        if (entry.getType() == DeepLinkEntry.Type.CLASS) {
          intent = new Intent(this, c);
        } else {
          Method method = c.getMethod(entry.getMethod(), Context.class);
          intent = (Intent) method.invoke(c, this);
        }

        Bundle parameters = new Bundle();
        for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet()) {
          parameters.putString(parameterEntry.getKey(), parameterEntry.getValue());
        }
        intent.putExtras(parameters);

        startActivity(intent);
      } catch (ClassNotFoundException exception) {
        Log.e(TAG, "Deep link to non-existent class: " + entry.getActivity());
      } catch (NoSuchMethodException exception) {
        Log.e(TAG, "Deep link to non-existent method: " + entry.getMethod());
      } catch (IllegalAccessException exception) {
        Log.e(TAG, "Could not deep link to method: " + entry.getMethod());
      } catch(InvocationTargetException  exception) {
        Log.e(TAG, "Could not deep link to method: " + entry.getMethod());
      }
    } else {
      Log.e(TAG, "No registered entity to handle deep link: " + uri.toString());
    }

    finish();
  }
}
