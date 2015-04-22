package com.airbnb.deeplinkdispatch.sample;

import android.app.Activity;
import android.os.Bundle;

public class DeepLinkActivity extends Activity {

  private DeepLinkDispatch dispatch = new DeepLinkDispatch();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    dispatch.handleDeepLink(this, getIntent());

    finish();
  }
}
