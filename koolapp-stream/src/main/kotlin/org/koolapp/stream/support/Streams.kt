package org.koolapp.stream.support

import org.koolapp.stream.*
import java.io.Closeable
import java.util.concurrent.Executor


/**
 * A [[Stream]] which invokes the given function on the handler
 */
class FunctionStream<T>(val fn: (Handler<T>) -> Unit): Stream<T>() {
    override fun open(handler: Handler<T>): Cursor {
        (fn)(handler)
        return DefaultCursor()
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
        val result = (fn)(handler)
        return delegate.open(result)
    }
}

/**
 * Creates an [[Stream]] which takes elements from the delegate stream until the predicate is false
 * then the stream closes the delegate stream
 */
class TakeWhileStream<T>(val delegate: Stream<T>, val predicate: (T) -> Boolean) : Stream<T>() {

    public override fun open(handler: Handler<T>): Cursor {
        val result = TakeWhileHandler(handler, predicate)
        val closeable = delegate.open(result)
        result.closeable = closeable
        return result

    }
}
/**
 * Creates an [[Stream]] which transforms the handler using the given function
 */
class MapStream<T,R>(val delegate: Stream<T>, val fn: (Handler<R>) -> Handler<T>) : Stream<R>() {

    public override fun open(handler: Handler<R>): Cursor {
        val result = (fn)(handler)
        return delegate.open(result)
    }
}