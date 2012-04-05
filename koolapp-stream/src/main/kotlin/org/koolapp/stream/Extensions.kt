package org.koolapp.stream

import org.koolapp.stream.support.*
import kotlin.concurrent.*

import java.util.*
import java.util.concurrent.Executor

/**
 * Converts a collection into an event stream
 */
fun <T> java.lang.Iterable<T>.toStream(executor: Executor = SynchronousExecutor()): Stream<T> = StreamCollection(this, executor)


/**
 * Creates a [[Stream]] of time events with the given millisecond *period* between events starting after the *delay* in milliseconds
 */
fun Timer.intervalStream(period: Long, delay: Long = 0): Stream<Long> {
    val timer = this
    return FunctionStream<Long> { (handler) ->
        val task = timer.scheduleAtFixedRate(delay, period) {
            val time = System.currentTimeMillis()
            handler.onNext(time)
        }
        val cursor = TimerTaskCursor(task)
        handler.onOpen(cursor)
        cursor
    }
}