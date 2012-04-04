package org.koolapp.reactive

import java.io.Closeable
import org.koolapp.reactive.support.*

/**
 * Represents an asynchronous stream of events which can be composed and processed asynchronously.
 *
 * You can think of a *Stream* as being like a push based collection where rather than pulling values out,
 * values are pushed into a [[handler]] instead so processing of collections can be done completely asynchronously.
 */
abstract class Stream<out T> {

    /**
     * Opens the stream for processing with the given handler
     */
    abstract fun open(handler: Handler<T>): Closeable

    /**
     * Opens the stream of events using the given function block to process each event
     * until the stream completes or fails
     */
    fun open(nextBlock: (T) -> Unit): Closeable {
        return open(FunctionHandler(nextBlock))
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

    class object {

        /**
         * Create an empty [[Stream]]
         */
        fun <T> empty(): Stream<T> = FunctionStream {
            it.onComplete()
        }

        /**
         * Create an [[Stream]] of a single value
         */
        fun <T> single(next: T): Stream<T> = FunctionStream {
            it.onNext(next)
            it.onComplete()
        }

        /**
         * Create an [[Stream]] which always raises an error
         */
        fun <T> error(exception: Exception): Stream<T> = FunctionStream {
            it.onError(exception)
        }

    }
}

