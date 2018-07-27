package com.airbnb.deeplinkdispatch.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.airbnb.deeplinkdispatch.sample.library.LibraryDeepLink;

/**
 * Main airbnb activity to show case {@link AppDeepLink}, {@link WebDeepLink} and {@link LibraryDeepLink}.
 */
@AppDeepLink({ "/view_users" })
@WebDeepLink({ "/users", "/user/{id}" })
@LibraryDeepLink({ "/library_deeplink", "/library_deeplink/{lib_id}" })
public class CustomPrefixesActivity extends AppCompatActivity {
  private static final String TAG = CustomPrefixesActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Bundle parameters = getIntent().getExtras();
      Log.d(TAG, "Deeplink params: " + parameters);

      String idString = parameters.getString("id");
      if (!TextUtils.isEmpty(idString)) {
        showToast("class id== " + idString);
      } else {
        showToast("no id in the deeplink");
      }
    } else {
      showToast("no deep link :( ");
    }
  }

  private void showToast(String message) {
    Toast.makeText(this, "Deep Link: " + message, Toast.LENGTH_SHORT).show();
  }
}
