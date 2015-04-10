package com.airbnb.deeplinkdispatch.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;


public class MainActivity extends ActionBarActivity {

  private static String ACTION_DEEP_LINK_ALPHA = "deep_link_alpha";
  private static String ACTION_DEEP_LINK_BETA = "deep_link_beta";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (getIntent().getAction().equals(ACTION_DEEP_LINK_ALPHA)) {
      showToast("ALPHA");
    } else if (getIntent().getAction().equals(ACTION_DEEP_LINK_BETA)) {
      showToast("BETA");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @DeepLink(uri = "airbnb://alpha")
  public static Intent intentForDeepLinkAlpha(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.setAction(ACTION_DEEP_LINK_ALPHA);
    return intent;
  }

  @DeepLink(uri = "airbnb://beta")
  public static Intent intentForDeepLinkBeta(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.setAction(ACTION_DEEP_LINK_BETA);
    return intent;
  }

  private void showToast(String message) {
    Toast.makeText(this, "Deep Link" + message, Toast.LENGTH_SHORT).show();
  }
}
