package com.airbnb.deeplinkdispatch

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Configure DeepLinkDispatch, such as for tests.
 */
object DeepLinkDispatch {
 /**
  * Executor for com.airbnb.deeplinkdispatch.ValidationUtilsKt.validateConfigurablePathSegmentReplacements
  */
 @JvmStatic
 var validationExecutor: Executor = Executors.newSingleThreadExecutor()
}