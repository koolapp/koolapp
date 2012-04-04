package org.koolapp.reactive

import org.koolapp.reactive.support.AbstractCursor

import java.io.Closeable
import java.util.ArrayList
import java.util.Collection
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * A simple [[Stream]] that can have elements injected into its active [[Handler]] instances
 */
class SimpleStream<in T>(val handlers: Collection<Handler<T>> = ConcurrentLinkedQueue<Handler<T>>()): Stream<T>(), Handler<T> {

    override fun open(handler: Handler<T>): Cursor {
        handlers.add(handler)
        return object: AbstractCursor() {
            protected override fun doClose() {
                handlers.remove(handler)
            }
        }
    }

    override fun onComplete(): Unit {
        forEach{ it.onComplete() }
    }

    override fun onError(e: Throwable): Unit {
        forEach{ it.onError(e) }
    }

    override fun onNext(next: T): Unit {
        forEach{ it.onNext(next) }
    }

    protected fun forEach(fn: (Handler<T>) -> Any): Unit {
        for (handler in handlers) {
            (fn)(handler)
        }
    }

}