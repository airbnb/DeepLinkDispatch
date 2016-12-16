package com.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.DeepLinkEntry;
import com.airbnb.deeplinkdispatch.DeepLinkHandler;
import com.airbnb.deeplinkdispatch.DeepLinkResult;
import com.airbnb.deeplinkdispatch.DeepLinkUri;
import com.airbnb.deeplinkdispatch.Parser;
import java.lang.NoSuchMethodException;
import java.lang.NullPointerException;
import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class DeepLinkDelegate {
  private static final String TAG = DeepLinkDelegate.class.getSimpleName();

  private final List<? extends Parser> loaders;

  public DeepLinkDelegate(SampleModuleLoader sampleModuleLoader) {
    this.loaders = Arrays.asList(
        sampleModuleLoader);
  }

  private DeepLinkEntry findEntry(String uriString) {
    for (Parser loader : loaders) {
      DeepLinkEntry entry = loader.parseUri(uriString);
      if (entry != null) {
        return entry;
      }
    }
    return null;
  }

  public DeepLinkResult dispatchFrom(Activity activity) {
    if (activity == null) {
      throw new NullPointerException("activity == null");
    }
    return dispatchFrom(activity, activity.getIntent());
  }

  public DeepLinkResult dispatchFrom(Activity activity, Intent sourceIntent) {
    if (activity == null) {
      throw new NullPointerException("activity == null");
    }
    if (sourceIntent == null) {
      throw new NullPointerException("sourceIntent == null");
    }
    Uri uri = sourceIntent.getData();
    if (uri == null) {
      return createResultAndNotify(activity, false, null, "No Uri in given activity's intent.");
    }
    String uriString = uri.toString();
    DeepLinkEntry entry = findEntry(uriString);
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
      Bundle parameters;
      if (sourceIntent.getExtras() != null) {
        parameters = new Bundle(sourceIntent.getExtras());
      } else {
        parameters = new Bundle();
      }
      for (Map.Entry<String, String> parameterEntry : parameterMap.entrySet()) {
        parameters.putString(parameterEntry.getKey(), parameterEntry.getValue());
      }
      try {
        Class<?> c = entry.getActivityClass();
        Intent newIntent;
        TaskStackBuilder taskStackBuilder = null;
        if (entry.getType() == DeepLinkEntry.Type.CLASS) {
          newIntent = new Intent(activity, c);
        } else {
          Method method;
          try {
            method = c.getMethod(entry.getMethod(), Context.class);
            if (method.getReturnType().equals(TaskStackBuilder.class)) {
              taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity);
              if (taskStackBuilder.getIntentCount() == 0) {
                return createResultAndNotify(activity, false, uri, "Could not deep link to method: " + entry.getMethod() + " intents length == 0" );
              }
              newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.getIntentCount()-1);
            } else {
              newIntent = (Intent) method.invoke(c, activity);
            }
          } catch (NoSuchMethodException exception) {
            method = c.getMethod(entry.getMethod(), Context.class, Bundle.class);
            if (method.getReturnType().equals(TaskStackBuilder.class)) {
              taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity, parameters);
              if (taskStackBuilder.getIntentCount() == 0) {
                return createResultAndNotify(activity, false, uri, "Could not deep link to method: " + entry.getMethod() + " intents length == 0" );
              }
              newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.getIntentCount()-1);
            } else {
              newIntent = (Intent) method.invoke(c, activity, parameters);
            }
          }
        }
        if (newIntent.getAction() == null) {
          newIntent.setAction(sourceIntent.getAction());
        }
        if (newIntent.getData() == null) {
          newIntent.setData(sourceIntent.getData());
        }
        newIntent.putExtras(parameters);
        newIntent.putExtra(DeepLink.IS_DEEP_LINK, true);
        newIntent.putExtra(DeepLink.REFERRER_URI, uri);
        if (activity.getCallingActivity() != null) {
          newIntent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        }
        if (taskStackBuilder != null) {
          taskStackBuilder.startActivities();
        } else {
          activity.startActivity(newIntent);
        }
        return createResultAndNotify(activity, true, uri, null);
      } catch (NoSuchMethodException exception) {
        return createResultAndNotify(activity, false, uri, "Deep link to non-existent method: " + entry.getMethod());
      } catch (IllegalAccessException exception) {
        return createResultAndNotify(activity, false, uri, "Could not deep link to method: " + entry.getMethod());
      } catch (InvocationTargetException  exception) {
        return createResultAndNotify(activity, false, uri, "Could not deep link to method: " + entry.getMethod());
      }
    } else {
      return createResultAndNotify(activity, false, uri, "No registered entity to handle deep link: " + uri.toString());
    }
  }

  private static DeepLinkResult createResultAndNotify(Context context, final boolean successful,
      final Uri uri, final String error) {
    notifyListener(context, !successful, uri, error);
    return new DeepLinkResult(successful, uri != null ? uri.toString() : null, error);
  }

  private static void notifyListener(Context context, boolean isError, Uri uri,
      String errorMessage) {
    Intent intent = new Intent();
    intent.setAction(DeepLinkHandler.ACTION);
    intent.putExtra(DeepLinkHandler.EXTRA_URI, uri != null ? uri.toString() : "");
    intent.putExtra(DeepLinkHandler.EXTRA_SUCCESSFUL, !isError);
    if (isError) {
      intent.putExtra(DeepLinkHandler.EXTRA_ERROR_MESSAGE, errorMessage);
    }
    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
  }

  public boolean supportsUri(String uriString) {
    return findEntry(uriString) != null;
  }
}
