package com.airbnb.deeplinkdispatch.sample.library;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;

@DeepLink("http://example.com/library")
public class LibraryActivity extends AppCompatActivity {
  @SuppressLint("RestrictedApi")
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
      Toast.makeText(this, "Got deep link " + intent.getStringExtra(DeepLink.URI),
          Toast.LENGTH_SHORT).show();
    }
  }
}
