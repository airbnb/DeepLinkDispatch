package com.airbnb.deeplinkdispatch;

public class Optional<T> {
  private static final Optional<?> ABSENT = new Optional<>();
  private final T value;

  private Optional() {
    this.value = null;
  }

  private Optional(T value) {
    if (value == null) {
      throw new NullPointerException();
    }
    this.value = value;
  }

  @SuppressWarnings("unchecked")
  public static <T> Optional<T> absent() {
    return (Optional<T>) ABSENT;
  }

  public static <T> Optional<T> of(T value) {
    return new Optional<>(value);
  }

  public T get() {
    if (value == null) {
      throw new NullPointerException("value is not present.");
    }
    return value;
  }

  public boolean isPresent() {
    return value != null;
  }
}
