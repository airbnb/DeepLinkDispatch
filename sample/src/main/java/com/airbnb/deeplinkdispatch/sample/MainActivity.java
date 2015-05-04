package com.airbnb.deeplinkdispatch.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;


@DeepLink(uri = "airbnb://classDeepLink")
public class MainActivity extends ActionBarActivity {

  private static String ACTION_DEEP_LINK_METHOD = "deep_link_method";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (ACTION_DEEP_LINK_METHOD.equals(getIntent().getAction())) {
      Bundle parameters = getIntent().getExtras();
      String param1 = parameters.getString("param1");
      showToast(param1);
    } else {
      showToast("class");
    }
  }

  @DeepLink(uri = "airbnb://methodDeepLink/{param1}")
  public static Intent intentForDeepLinkMethod(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.setAction(ACTION_DEEP_LINK_METHOD);
    return intent;
  }

  private void showToast(String message) {
    Toast.makeText(this, "Deep Link: " + message, Toast.LENGTH_SHORT).show();
  }
}
