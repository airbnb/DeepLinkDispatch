package com.airbnb.deeplinkdispatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.lang.Override;
import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class DeepLinkActivity extends Activity {
  private static final String TAG = DeepLinkActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Loader loader = new com.airbnb.deeplinkdispatch.DeepLinkLoader();
    DeepLinkRegistry registry = new DeepLinkRegistry(loader);
    Uri uri = getIntent().getData();
    String hostPath = uri.getHost() + uri.getPath();
    DeepLinkEntry entry = registry.parseUri(hostPath);
    if (entry != null) {
      Map<String, String> parameterMap = entry.getParameters(hostPath);
      for (String queryParameter : uri.getQueryParameterNames()) {
        if (parameterMap.containsKey(queryParameter)) {
          Log.w(TAG, "Duplicate parameter name in path and query param: " + queryParameter);
        }
        parameterMap.put(queryParameter, uri.getQueryParameter(queryParameter));
      }
      try {
        Class<?> c = entry.getActivityClass();
        Intent intent;
        if (entry.getType() == DeepLinkEntry.Type.CLASS) {
          intent = new Intent(this, c);
        } else {
          Method method = c.getMethod(entry.getMethod(), Context.class);
          intent = (Intent) method.invoke(c, this);
        }
        if (intent.getAction() == null) {
          intent.setAction(getIntent().getAction());
        }
        if (intent.getData() == null) {
          intent.setData(getIntent().getData());
        }
        Bundle parameters;
        if (getIntent().getExtras() != null) {
          parameters = new Bundle(getIntent().getExtras());
        } else {
          parameters = new Bundle();
        }
        for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet()) {
          parameters.putString(parameterEntry.getKey(), parameterEntry.getValue());
        }
        intent.putExtras(parameters);
        intent.putExtra(DeepLink.IS_DEEP_LINK, true);
        startActivity(intent);
        notifyListener(false, uri, null);
      } catch (NoSuchMethodException exception) {
        notifyListener(true, uri, "Deep link to non-existent method: " + entry.getMethod());
      } catch (IllegalAccessException exception) {
        notifyListener(true, uri, "Could not deep link to method: " + entry.getMethod());
      } catch(InvocationTargetException  exception) {
        notifyListener(true, uri, "Could not deep link to method: " + entry.getMethod());
      } finally {
        finish();
      }
    } else {
      notifyListener(true, uri, "No registered entity to handle deep link: " + uri.toString());
      finish();
    }
  }

  private void notifyListener(boolean isError, Uri uri, String errorMessage) {
    if (getApplication() instanceof DeepLinkCallback) {
      DeepLinkCallback listener = (DeepLinkCallback) getApplication();
      if (!isError) {
        listener.onSuccess(uri.toString());
      } else {
        listener.onError(new DeepLinkError(uri.toString(), errorMessage));
      }
    }
  }
}
