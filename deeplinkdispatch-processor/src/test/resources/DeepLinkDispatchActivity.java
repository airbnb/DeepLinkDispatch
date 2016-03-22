package com.airbnb.deeplinkdispatch;

import android.app.Activity;
import android.os.Bundle;
import java.lang.Override;

public class DeepLinkDispatchActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DeepLinkDelegate.dispatchFrom(this);
  }
}
