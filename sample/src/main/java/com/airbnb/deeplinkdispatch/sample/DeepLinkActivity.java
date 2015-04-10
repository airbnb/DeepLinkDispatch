package com.airbnb.deeplinkdispatch.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class DeepLinkActivity extends ActionBarActivity {

  private DeepLinkDispatch dispatch = new DeepLinkDispatch();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    dispatch.handleDeepLink(this, getIntent());
  }
}
