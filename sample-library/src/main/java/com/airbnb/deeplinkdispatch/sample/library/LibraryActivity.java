package com.airbnb.deeplinkdispatch.sample.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;

@DeepLink("http://example.com/library")
public class LibraryActivity extends AppCompatActivity {

  private static final String TAG = LibraryActivity.class.getSimpleName();

  @SuppressLint("RestrictedApi")
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Toast.makeText(this, "Got deep link " + intent.getStringExtra(DeepLink.URI),
          Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * This will not create an error during index creation but could be found by writing a test
   * There is another example method in the main sample apps `MainActivity`
   */
  @DeepLink("dld://host/intent/{geh}")
  static Intent sampleDuplicatedUrlWithDifferentPlaceholderNameInLib(Context context) {
    return null;
  }

  /**
   * This method is a more concrete match for the URI dld://host/somePathOne/somePathTwo/somePathThree
   * to a annotated method in `sample` that is annotated with
   * @DeepLink("dld://host/somePathOne/{param1}/somePathThree") and thus will never be picked over
   * this method when matching.
   *
   * @param context
   * @param bundle
   * @return
   */
  @DeepLink("placeholder://host/somePathOne/somePathTwo/somePathThree")
  public static Intent moreConcreteMatch(Context context, Bundle bundle) {
    Log.d(TAG, "matched more concrete url in sample-library project.");
    return new Intent(context, LibraryActivity.class);
  }
}
