package com.airbnb.deeplinkdispatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.deeplinkdispatch.base.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
public class BaseDeepLinkDelegate {

  protected static final String TAG = "DeepLinkDelegate";

  protected final List<? extends BaseRegistry> registries;

  @Nullable
  protected final ErrorHandler errorHandler;
  /**
   * <p>Mapping of values for DLD to substitute for annotation-declared configurablePathSegments.
   * </p>
   * <p>Example</p>
   * Given:
   * <ul>
   * <li><xmp>@DeepLink("https://www.example.com/<configurable-path-segment>/users/{param1}")
   * </xmp></li>
   * <li>mapOf("pathVariableReplacementValue" to "obamaOs")</li>
   * </ul>
   * Then:
   * <ul><li><xmp>https://www.example.com/obamaOs/users/{param1}</xmp> will match.</li></ul>
   */
  protected final Map<byte[], byte[]> configurablePathSegmentReplacements;

  public List<? extends BaseRegistry> getRegistries() {
    return registries;
  }

  public BaseDeepLinkDelegate(List<? extends BaseRegistry> registries) {
    this.registries = registries;
    this.errorHandler = null;
    configurablePathSegmentReplacements = new HashMap<>();
    ValidationUtilsKt.validateConfigurablePathSegmentReplacements(registries,
      this.configurablePathSegmentReplacements);
  }

  public BaseDeepLinkDelegate(List<? extends BaseRegistry> registries, ErrorHandler errorHandler) {
    this.registries = registries;
    this.errorHandler = errorHandler;
    configurablePathSegmentReplacements = new HashMap<>();
    ValidationUtilsKt.validateConfigurablePathSegmentReplacements(registries,
      this.configurablePathSegmentReplacements);
  }

  public BaseDeepLinkDelegate(
    List<? extends BaseRegistry> registries,
    Map<String, String> configurablePathSegmentReplacements
  ) {
    this.registries = registries;
    this.errorHandler = null;
    this.configurablePathSegmentReplacements =
      Utils.toByteArrayMap(configurablePathSegmentReplacements);
    ValidationUtilsKt.validateConfigurablePathSegmentReplacements(registries,
      this.configurablePathSegmentReplacements);
  }

  public BaseDeepLinkDelegate(
    List<? extends BaseRegistry> registries,
    Map<String, String> configurablePathSegmentReplacements,
    ErrorHandler errorHandler
  ) {
    this.registries = registries;
    this.errorHandler = errorHandler;
    this.configurablePathSegmentReplacements =
      Utils.toByteArrayMap(configurablePathSegmentReplacements);
    ValidationUtilsKt.validateConfigurablePathSegmentReplacements(registries,
      this.configurablePathSegmentReplacements);
  }

  /**
   * Calls into {@link #dispatchFrom(Activity activity, Intent sourceIntent)}.
   *
   * @param activity activity with inbound Intent stored on it.
   * @return DeepLinkResult, whether success or error.
   */
  public DeepLinkResult dispatchFrom(Activity activity) {
    validateInput(activity);
    return dispatchFrom(activity, activity.getIntent());
  }

  /**
   * Calls {@link #createResult(Activity, Intent, DeepLinkEntry)}. If the DeepLinkResult has
   * a non-null TaskStackBuilder or Intent, it starts it. It always calls
   * {@link #notifyListener(Context, boolean, Uri, String, String)}.
   *
   * @param activity     used to startActivity() or notifyListener().
   * @param sourceIntent inbound Intent.
   * @return DeepLinkResult
   */
  public DeepLinkResult dispatchFrom(Activity activity, Intent sourceIntent) {
    validateInput(activity, sourceIntent);
    Uri uri = sourceIntent.getData();
    DeepLinkResult result;
    if (uri == null) {
      result = createResult(activity, sourceIntent, null);
    } else {
      result = createResult(activity, sourceIntent, findEntry(uri.toString()));
    }
    if (result.getTaskStackBuilder() != null) {
      result.getTaskStackBuilder().startActivities();
    } else if (result.getIntent() != null) {
      activity.startActivity(result.getIntent());
    }
    notifyListener(activity, !result.isSuccessful(), uri,
      result.getDeepLinkEntry() != null ? result.getDeepLinkEntry().getUriTemplate()
        : null, result.getError());
    return result;
  }

  /**
   * Create a {@link DeepLinkResult}, whether we are able to match the uri on
   * {@param sourceIntent} or not.
   *
   * @param activity      used to startActivity() or notifyListener().
   * @param sourceIntent  inbound Intent.
   * @param deepLinkEntry deepLinkEntry that matches the sourceIntent's URI. Derived from
   *                      {@link #findEntry(String)}. Can be injected for testing.
   * @return DeepLinkResult
   */
  public @NonNull DeepLinkResult createResult(
    Activity activity, Intent sourceIntent, DeepLinkEntry deepLinkEntry
  ) {
    validateInput(activity, sourceIntent);
    Uri uri = sourceIntent.getData();
    if (uri == null) {
      return new DeepLinkResult(
        false, null, "No Uri in given activity's intent.", null, null, deepLinkEntry);
    }
    String uriString = uri.toString();
    if (deepLinkEntry == null) {
      return new DeepLinkResult(false, null, "DeepLinkEntry cannot be null",
        null, null, null);
    }
    DeepLinkUri deepLinkUri = DeepLinkUri.parse(uriString);
    Map<String, String> parameterMap = new HashMap<>(deepLinkEntry.getParameters(deepLinkUri));
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
      Class<?> c = deepLinkEntry.getActivityClass();
      Intent newIntent = null;
      TaskStackBuilder taskStackBuilder = null;
      if (deepLinkEntry.getType() == DeepLinkEntry.Type.CLASS) {
        newIntent = new Intent(activity, c);
      } else {
        Method method;
        DeepLinkResult errorResult = new DeepLinkResult(false, uriString,
          "Could not deep link to method: " + deepLinkEntry.getMethod() + " intents length == 0",
          null, null, deepLinkEntry);
        try {
          method = c.getMethod(deepLinkEntry.getMethod(), Context.class);
          if (method.getReturnType().equals(TaskStackBuilder.class)) {
            taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity);
            if (taskStackBuilder.getIntentCount() == 0) {
              return errorResult;
            }
            newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.getIntentCount() - 1);
          } else {
            newIntent = (Intent) method.invoke(c, activity);
          }
        } catch (NoSuchMethodException exception) {
          method = c.getMethod(deepLinkEntry.getMethod(), Context.class, Bundle.class);
          if (method.getReturnType().equals(TaskStackBuilder.class)) {
            taskStackBuilder = (TaskStackBuilder) method.invoke(c, activity, parameters);
            if (taskStackBuilder.getIntentCount() == 0) {
              return errorResult;
            }
            newIntent = taskStackBuilder.editIntentAt(taskStackBuilder.getIntentCount() - 1);
          } else {
            newIntent = (Intent) method.invoke(c, activity, parameters);
          }
        }
      }
      if (newIntent == null) {
        return new DeepLinkResult(false, uriString, "Destination Intent is null!", null,
          taskStackBuilder, deepLinkEntry);
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
      return new DeepLinkResult(true, uriString, "", newIntent, taskStackBuilder, deepLinkEntry);
    } catch (NoSuchMethodException exception) {
      return new DeepLinkResult(false, uriString, "Dee3p link to non-existent method: "
        + deepLinkEntry.getMethod(), null, null, deepLinkEntry);
    } catch (IllegalAccessException exception) {
      return new DeepLinkResult(false, uriString, "Could not deep link to method: "
        + deepLinkEntry.getMethod(), null, null, deepLinkEntry);
    } catch (InvocationTargetException exception) {
      return new DeepLinkResult(false, uriString, "Could not deep link to method: "
        + deepLinkEntry.getMethod(), null, null, deepLinkEntry);
    }
  }

  private DeepLinkEntry findEntry(String uriString) {
    DeepLinkEntry entryRegExpMatch = null;
    List<DeepLinkEntry> entryIdxMatches = new ArrayList<>();
    DeepLinkUri uri = DeepLinkUri.parse(uriString);
    for (BaseRegistry registry : registries) {
      DeepLinkEntry entryIdxMatch = registry.idxMatch(uri, configurablePathSegmentReplacements);
      if (entryIdxMatch != null) {
        entryIdxMatches.add(entryIdxMatch);
      }
    }
    // Found no match
    if (entryIdxMatches.isEmpty()) {
      return null;
    } else if (entryIdxMatches.size() == 1) {
      // Found just one match
      return entryIdxMatches.get(0);
    }
    // Found multiple matches. Sort matches by concreteness:
    // No variable element > containing placeholders >  are a configurable path segment
    Collections.sort(entryIdxMatches);
    if (entryIdxMatches.get(0).compareTo(entryIdxMatches.get(1)) == 0) {
      if (errorHandler != null) {
        errorHandler.duplicateMatch(uriString, entryIdxMatches.subList(0, 2));
      }
      Log.w(TAG, "More than one match with the same concreteness!! ("
        + entryIdxMatches.get(0).toString() + ") vs. (" + entryIdxMatches.get(1).toString() + ")");
    }
    return entryIdxMatches.get(0);
  }

  private void validateInput(Activity activity, Intent sourceIntent) {
    validateInput(activity);
    validateInput(sourceIntent);
  }

  private void validateInput(Activity activity) {
    if (activity == null) {
      throw new NullPointerException("activity == null");
    }
  }

  private void validateInput(Intent sourceIntent) {
    if (sourceIntent == null) {
      throw new NullPointerException("sourceIntent == null");
    }
  }

  private static void notifyListener(Context context, boolean isError, Uri uri,
                                     String uriTemplate, String errorMessage) {
    Intent intent = new Intent();
    intent.setAction(DeepLinkHandler.ACTION);
    intent.putExtra(DeepLinkHandler.EXTRA_URI, uri != null ? uri.toString() : "");
    intent.putExtra(DeepLinkHandler.EXTRA_URI_TEMPLATE, uriTemplate != null ? uriTemplate : "");
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
