package org.koolapp.stream

import java.io.Closeable
import org.koolapp.stream.support.*
import java.util.Queue
import java.util.List

/**
 * Represents an asynchronous stream of events which can be composed and processed asynchronously.
 *
 * You can think of a *Stream* as being like a push based collection where rather than pulling values out,
 * values are pushed into a [[handler]] instead so processing of collections can be done completely asynchronously.
 */
public abstract class Stream<out T> {

    /**
     * Opens the stream for processing with the given handler
     */
    abstract fun open(handler: Handler<T>): Cursor

    /**
     * Opens the stream of events using the given function block to process each event
     * until the stream completes or fails
     */
    public open fun open(nextBlock: (T) -> Unit): Cursor {
        return open(FunctionHandler(nextBlock))
    }

    /**
     * Opens the stream of events using the given function block to process each event
     * returning *false* if the next event cannot be processed yet to allow flow control to
     * kick in
     */
    public open fun openSuspendable(nextBlock: (T) -> Boolean): Cursor {
        return open(FunctionSuspendableHandler(nextBlock))
    }

    /**
     * Opens the stream of events using a [[SuspendableHandler]] so that flow control
     * can be used to suspend the stream if the handler cannot consume an offered next event.
     *
     * [[Stream]] implementation classes which can implement flow control should override this
     * function to provide support for the [[SuspendableCursor]]
     */
    public open fun open(suspendableHandler: SuspendableHandler<T>): SuspendableCursor {
        val handler = SuspendableHandlerAdapter(suspendableHandler)
        val cursor = open(handler)
        return cursor.toSuspendableCursor()
    }

    /**
     * Returns a new [[Stream]] which filters out elements
     * in the stream to those which match the given predicate
     */
    fun filter(predicate: (T) -> Boolean): Stream<T>  {
        return DelegateStream(this) {
            FilterHandler(it, predicate)
        }
    }

    /**
     * Returns a new [[Stream]] which transforms the elements
     * in the stream using the given function
     */
    fun <R> map(transform: (T) -> R): Stream<R> {
        return MapStream<T,R>(this) {
            MapHandler<T,R>(it, transform)
        }
    }

    /**
     * Returns a [[Stream]] which filters out duplicates
     */
    fun distinct(): Stream<T> {
        var previous: T? = null
        return filter  {
            val changed = previous == null || previous != it
            previous = it
            changed
        }
    }

    /**
     * Returns a [[Stream]] which takes the given amount of items from
     * the stream then closes it
     */
    fun take(n: Int): Stream<T> {
        var counter = n
        return TakeWhileStream(this, true){ --counter > 0 }
    }

    /**
     * Returns a [[Stream]] which takes the events from this stream until
     * the given predicate returns false and the underlying stream is then closed
     */
    fun takeWhile(predicate: (T) -> Boolean): Stream<T> {
        return TakeWhileStream(this, false, predicate)
    }

    /**
     * Returns a [[Stream]] which consumes events from *this* stream and *that* stream
     * and then raises events when *this* stream then *that* stream receive an event.
     * Multiple events on either stream in between are discarded.
     */
    fun <R> then(stream: Stream<R>): Stream<#(T,R)> {
        return ThenStream(this, stream)
    }

    /**
     * Returns a [[Stream]] which consumes events and puts them into a moving time window
     * of a given number of *millis* which then fires the window [[List]] of elements into the handler on each event
     */
    fun timeWindow(millis: Long): Stream<List<T>> {
        return TimeWindowStream(this, millis)
    }

    /**
     * Returns a [[Stream]] which consumes events and puts them into a moving window
     * of a fixed *size* which then fires the window [[List]] of elements into the handler on each event
     */
    fun window(size: Int): Stream<List<T>> {
        return WindowStream(this, size)
    }

    /**
     * Helper method to open a delegate stream
     */
    protected fun openDelegate(delegate: Stream<T>, handler: Handler<T>): Cursor {
        val cursor = delegate.open(handler)
        handler.onOpen(cursor)
        return cursor
    }

    class object {

        /**
         * Create an empty [[Stream]]
         */
        fun <T> empty(): Stream<T> = FunctionStream {
            it.onComplete()
            DefaultCursor()
        }

        /**
         * Create an [[Stream]] of a single value
         */
        fun <T> single(next: T): Stream<T> = FunctionStream {
            it.onNext(next)
            it.onComplete()
            DefaultCursor()
        }

        /**
         * Create an [[Stream]] which always raises an error
         */
        fun <T> error(exception: Throwable): Stream<T> = FunctionStream {
            it.onError(exception)
            DefaultCursor()
        }

    }
}

