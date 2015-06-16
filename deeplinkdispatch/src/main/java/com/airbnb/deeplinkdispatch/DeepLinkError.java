/*
 * Copyright (C) 2015 Airbnb, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.airbnb.deeplinkdispatch;

/**
 * Error class containing information of deep links that failed to dispatch.
 * Contains the URI that failed to be dispatched and the error message.
 */
public class DeepLinkError {
  public final String uri;
  public final String errorMessage;

  public DeepLinkError(String uri, String errorMessage) {
    this.uri = uri;
    this.errorMessage = errorMessage;
  }

  /**
   * @return the URI of the deep link that causes the failure
   */
  public String getUri() {
    return uri;
  }

  /**
   * @return the error message when calling the deep link
   */
  public String getErrorMessage() {
    return errorMessage;
  }
}
