package com.airbnb.deeplinkdispatch.sample.typeconversion;

import android.graphics.drawable.ColorDrawable;

import androidx.annotation.Nullable;

/**
 * So we can actually use this in tests to comapre to expected.
 */
public class ComparableColorDrawable extends ColorDrawable {
  public ComparableColorDrawable(int i) {
    super(i);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    return obj instanceof ColorDrawable && ((ColorDrawable) obj).getColor() == this.getColor();
  }
}
