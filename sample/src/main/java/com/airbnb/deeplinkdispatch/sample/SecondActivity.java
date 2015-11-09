package com.airbnb.deeplinkdispatch.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.airbnb.deeplinkdispatch.DeepLink;

@DeepLink("dld://example.com/deepLink/{id}")
public class SecondActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
