package com.airbnb.deeplinkdispatch.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;


@DeepLink(host = "classDeepLink")
// You can also register multiple deep links for a particular activity to handle:
// @DeepLinks({@DeepLink(host = "classDeepLink"), @DeepLink(host="anotherClassDeepLink")})
public class MainActivity extends AppCompatActivity {

  private static String ACTION_DEEP_LINK_METHOD = "deep_link_method";
  private static String ACTION_DEEP_LINK_COMPLEX = "deep_link_complex";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      String toastMessage;
      Bundle parameters = getIntent().getExtras();
      if (ACTION_DEEP_LINK_METHOD.equals(getIntent().getAction())) {
        toastMessage = parameters.getString("param1");
      } else if (ACTION_DEEP_LINK_COMPLEX.equals(getIntent().getAction())) {
        toastMessage = parameters.getString("arbitraryNumber");
      } else {
        toastMessage = "class";
      }

      // You can pass a query parameter with the URI, and it's also in parameters, like
      // airbnb://classDeepLink?qa=123
      if (parameters != null && parameters.getString("qp") != null) {
        toastMessage += " with query parameter " + parameters.getString("qp");
      }

      showToast(toastMessage);
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
