package org.koolapp.reactive

import java.util.*
import org.koolapp.reactive.support.*
import java.util.concurrent.Executor

/**
 * Converts a collection into an event stream
 */
fun <T> java.lang.Iterable<T>.toStream(executor: Executor = SynchronousExecutor()): Stream<T> = StreamCollection(this, executor)
