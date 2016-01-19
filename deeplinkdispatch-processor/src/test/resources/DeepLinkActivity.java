package com.airbnb.deeplinkdispatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import java.lang.Override;
import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class DeepLinkActivity extends Activity {
  private static final String TAG = DeepLinkActivity.class.getSimpleName();

  public static final String ACTION = "com.airbnb.deeplinkdispatch.DEEPLINK_ACTION";

  public static final String EXTRA_SUCCESSFUL = "com.airbnb.deeplinkdispatch.EXTRA_SUCCESSFUL";

  public static final String EXTRA_URI = "com.airbnb.deeplinkdispatch.EXTRA_URI";

  public static final String EXTRA_ERROR_MESSAGE = "com.airbnb.deeplinkdispatch.EXTRA_ERROR_MESSAGE";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DeepLinkLoader loader = new DeepLinkLoader();
    loader.load();
    Uri uri = getIntent().getData();
    String uriString = uri.toString();
    DeepLinkEntry entry = loader.parseUri(uriString);
    if (entry != null) {
      DeepLinkUri deepLinkUri = DeepLinkUri.parse(uriString);
      Map<String, String> parameterMap = entry.getParameters(uriString);
      for (String queryParameter : deepLinkUri.queryParameterNames()) {
        for (String queryParameterValue : deepLinkUri.queryParameterValues(queryParameter)) {
          if (parameterMap.containsKey(queryParameter)) {
            Log.w(TAG, "Duplicate parameter name in path and query param: " + queryParameter);
          }
          parameterMap.put(queryParameter, queryParameterValue);
        }
      }
      parameterMap.put(DeepLink.URI, uri.toString());
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
        if (getCallingActivity() != null) {
          intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        }
        startActivity(intent);
        notifyListener(false, uri, null);
      } catch (NoSuchMethodException exception) {
        notifyListener(true, uri, "Deep link to non-existent method: " + entry.getMethod());
      } catch (IllegalAccessException exception) {
        notifyListener(true, uri, "Could not deep link to method: " + entry.getMethod());
      } catch (InvocationTargetException  exception) {
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
    Intent intent = new Intent();
    intent.setAction(DeepLinkActivity.ACTION);
    intent.putExtra(DeepLinkActivity.EXTRA_URI, uri.toString());
    intent.putExtra(DeepLinkActivity.EXTRA_SUCCESSFUL, !isError);
    if (isError) {
      intent.putExtra(DeepLinkActivity.EXTRA_ERROR_MESSAGE, errorMessage);
    }
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }
}
