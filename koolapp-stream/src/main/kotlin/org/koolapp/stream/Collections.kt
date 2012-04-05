package org.koolapp.stream

import java.util.concurrent.Executor
import org.koolapp.stream.support.*

/**
* Converts a collection into an event stream
*/
fun <T> java.lang.Iterable<T>.toStream(executor: Executor = SynchronousExecutor()): Stream<T> = StreamCollection(this, executor)
