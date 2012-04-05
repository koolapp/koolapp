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
 * Creates a [[TimerTask]] for the given handler
 */
fun Handler<Long>.toTimerTask(): TimerTask {
    val handler = this
    return timerTask {
        val time = System.currentTimeMillis()
        handler.onNext(time)
    }
}

/**
 * Creates a [[Stream]] of time events with the given millisecond *period* between events starting after the *delay* in milliseconds
 */
fun Timer.intervalStream(period: Long, delay: Long = 0): Stream<Long> = TimerStream {
    scheduleAtFixedRate(it, delay, period)
}

/**
 * Creates a [[Stream]] of time events with the given millisecond *period* between events starting at the given *firstTime*
 */
fun Timer.intervalStream(period: Long, firstTime: Date): Stream<Long> = TimerStream {
    scheduleAtFixedRate(it, firstTime, period)
}
