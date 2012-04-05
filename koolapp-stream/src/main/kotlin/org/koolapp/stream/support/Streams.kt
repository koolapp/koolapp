package org.koolapp.stream.support

import org.koolapp.stream.*
import java.io.Closeable
import java.util.concurrent.Executor
import java.util.Timer
import java.util.TimerTask


/**
 * A [[Stream]] which invokes the given function to open a stream and return a cursor
 */
class FunctionStream<T>(val fn: (Handler<T>) -> Cursor): Stream<T>() {
    fun toString() = "FunctionStream($fn)"

    public override fun open(handler: Handler<T>): Cursor {
        return (fn)(handler)
    }
}

/**
 * A [[Stream]] which uses a [[Timer]] to schedule the invocation of the [[Handler]] at specific
 * scheduled times defined by the *schedularFunction*
 */
class TimerStream(val schedularFunction: (TimerTask) -> Unit): Stream<Long>() {
    fun toString() = "TimerStream($schedularFunction)"

    public override fun open(handler: Handler<Long>): Cursor {
        val task = handler.toTimerTask()
        val cursor = TimerTaskCursor(task, handler)
        handler.onOpen(cursor)
        (schedularFunction)(task)
        return cursor
    }
}

/**
 * Converts a collection into an [[Stream]]
 */
class StreamCollection<T>(val coll: java.lang.Iterable<T>, val executor: Executor) : Stream<T>() {
    public override fun open(handler: Handler<T>): Cursor {
        val subscription = IteratorTask(coll.iterator(), handler)
        executor.execute(subscription)
        return subscription
    }

}

/**
 * Creates an [[Stream]] which transforms the handler using the given function
 */
class DelegateStream<T>(val delegate: Stream<T>, val fn: (Handler<T>) -> Handler<T>) : Stream<T>() {

    public override fun open(handler: Handler<T>): Cursor {
        val newHandler = (fn)(handler)
        return openDelegate(delegate, newHandler)
    }
}

/**
 * Creates an [[Stream]] which takes elements from the delegate stream until the *predicate* is false
 * then the stream closes the *delegate* stream.
 *
 * If *includeNonMatching* is true then the final value which caused the *predicate* to return false will
 * also be passed to the *delegate* stream
 */
class TakeWhileStream<T>(val delegate: Stream<T>, val includeNonMatching: Boolean, val predicate: (T) -> Boolean) : Stream<T>() {

    public override fun open(handler: Handler<T>): Cursor {
        val newHandler = TakeWhileHandler(handler, includeNonMatching, predicate)
        return openDelegate(delegate, newHandler)
    }
}
/**
 * Creates an [[Stream]] which transforms the handler using the given function
 */
class MapStream<T,R>(val delegate: Stream<T>, val fn: (Handler<R>) -> Handler<T>) : Stream<R>() {

    public override fun open(handler: Handler<R>): Cursor {
        val newHandler = (fn)(handler)
        val cursor = delegate.open(newHandler)
        newHandler.onOpen(cursor)
        return cursor
    }
}