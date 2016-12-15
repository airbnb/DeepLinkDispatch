package com.airbnb.deeplinkdispatch;

import javax.annotation.Nullable;

@SuppressWarnings({ "unused", "WeakerAccess" })
public final class DeepLinkResult {
  private final boolean successful;
  private final String error;
  @Nullable private final String uriString;

  public DeepLinkResult(boolean successful, @Nullable String uriString, String error) {
    this.successful = successful;
    this.uriString = uriString;
    this.error = error;
  }

  /** @return whether or not the dispatch was a success. */
  public boolean isSuccessful() {
    return successful;
  }

  /** @return this result's uri, or {@code null} if there is none. */
  @Nullable public String uriString() {
    return uriString;
  }

  /** @return this result's error message, or {@code null} if there is none. */
  public String error() {
    return error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DeepLinkResult that = (DeepLinkResult) o;

    if (successful != that.successful) {
      return false;
    }
    //noinspection SimplifiableIfStatement
    if (uriString != null ? !uriString.equals(that.uriString) : that.uriString != null) {
      return false;
    }
    return error != null ? error.equals(that.error) : that.error == null;
  }

  @Override
  public int hashCode() {
    int result = (successful ? 1 : 0);
    result = 31 * result + (uriString != null ? uriString.hashCode() : 0);
    result = 31 * result + (error != null ? error.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DeepLinkResult{"
        + "successful=" + successful
        + ", uriString=" + uriString
        + ", error='" + error + '\''
        + '}';
  }
}
