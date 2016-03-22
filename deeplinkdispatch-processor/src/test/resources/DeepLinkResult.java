package com.airbnb.deeplinkdispatch;

import android.net.Uri;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

public final class DeepLinkResult {
  private final boolean successful;

  private final Uri uri;

  private final String errorMessage;

  DeepLinkResult(boolean successful, Uri uri, String errorMessage) {
    this.successful = successful;
    this.uri = uri;
    this.errorMessage = errorMessage;
  }

  /**
   * @return whether or not the dispatch was a success.
   */
  public boolean isSuccessful() {
    return successful;
  }

  /**
   * @return this result's uri, or {@code null} if there is none.
   */
  public Uri uri() {
    return uri;
  }

  /**
   * @return this result's error message, or {@code null} if there is none.
   */
  public String errorMessage() {
    return errorMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    DeepLinkResult that = (DeepLinkResult) o;

    if (successful != that.successful) { return false; }
    if (uri != null ? !uri.equals(that.uri) : that.uri != null) { return false; }
    return errorMessage != null ? errorMessage.equals(that.errorMessage) : that.errorMessage == null;}

  @Override
  public int hashCode() {
    int result = (successful ? 1 : 0);
    result = 31 * result + (uri != null ? uri.hashCode() : 0);
    result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "DeepLinkResult{" +
            "successful=" + successful +
            ", uri=" + uri +
            ", errorMessage='" + errorMessage + '\'' +
            '}';}
}
