package com.airbnb.deeplinkdispatch.sample;

import android.content.Context;
import android.content.Intent;
import androidx.core.app.TaskStackBuilder;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;

import java.util.ArrayList;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(TaskStackBuilder.class)
public class ShadowTaskStackBuilder {
  private final ArrayList<Intent> mIntents = new ArrayList<>();
  @RealObject private TaskStackBuilder realTaskStackBuilder;

  @Implementation
  public static TaskStackBuilder create(Context context) {
    return Shadow.newInstanceOf(TaskStackBuilder.class);
  }

  @Implementation
  public TaskStackBuilder addNextIntent(Intent nextIntent) {
    mIntents.add(nextIntent);
    return realTaskStackBuilder;
  }

  @Implementation
  public void startActivities() {
    if (mIntents.isEmpty()) {
      throw new IllegalStateException(
          "No intents added to TaskStackBuilder; cannot startActivities");
    }
    Intent[] intents = mIntents.toArray(new Intent[mIntents.size()]);
    intents[0] = new Intent(intents[0]).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
        Intent.FLAG_ACTIVITY_CLEAR_TASK |
        Intent.FLAG_ACTIVITY_TASK_ON_HOME);
    RuntimeEnvironment.application.startActivities(intents);
  }

  @Implementation
  public int getIntentCount() {
    return mIntents.size();
  }

  @Implementation
  public Intent editIntentAt(int index) {
    return mIntents.get(index);
  }
}