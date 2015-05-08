package com.airbnb.deeplinkdispatch.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;


@DeepLink(host = "classDeepLink")
public class MainActivity extends AppCompatActivity {

  private static String ACTION_DEEP_LINK_METHOD = "deep_link_method";
  private static String ACTION_DEEP_LINK_COMPLEX = "deep_link_complex";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (ACTION_DEEP_LINK_METHOD.equals(getIntent().getAction())) {
      Bundle parameters = getIntent().getExtras();
      String param1 = parameters.getString("param1");
      showToast(param1);
    } else if (ACTION_DEEP_LINK_COMPLEX.equals(getIntent().getAction())) {
      Bundle parameters = getIntent().getExtras();
      String arbitraryNumber = parameters.getString("arbitraryNumber");
      showToast(arbitraryNumber);
    } else {
      showToast("class");
    }
  }

  @DeepLink(host = "methodDeepLink", path="{param1}")
  public static Intent intentForDeepLinkMethod(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.setAction(ACTION_DEEP_LINK_METHOD);
    return intent;
  }

  @DeepLink(host = "host", path="somePath/{arbitraryNumber}")
  public static Intent intentForComplexMethod(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.setAction(ACTION_DEEP_LINK_COMPLEX);
    return intent;
  }

  private void showToast(String message) {
    Toast.makeText(this, "Deep Link: " + message, Toast.LENGTH_SHORT).show();
  }
}
