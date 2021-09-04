package com.airbnb.deeplinkdispatch.sample

import java.util.concurrent.Executor

object TestUtils {
    @JvmStatic
    val immediateExecutor: Executor = Executor { runnable -> runnable.run() }
}
