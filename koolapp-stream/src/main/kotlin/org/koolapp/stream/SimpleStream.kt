package org.koolapp.stream

import org.koolapp.stream.support.*

import java.io.Closeable
import java.util.ArrayList
import java.util.Collection
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * A simple [[Stream]] that can have elements injected into its active [[Handler]] instances
 */
open class SimpleStream<in T>(val handlers: ConcurrentContainer<Handler<T>> = DefaultConcurrentContainer<Handler<T>>()): Stream<T>(), Handler<T> {

    override fun open(handler: Handler<T>): Cursor {
        handlers.add(handler)
        return object: AbstractCursor() {
            protected override fun doClose() {
                handlers.remove(handler)
            }
        }
    }

    override fun onComplete(): Unit {
        handlers.forEach{ it.onComplete() }
    }

    override fun onError(e: Throwable): Unit {
        handlers.forEach{ it.onError(e) }
    }

    override fun onNext(next: T): Unit {
        handlers.forEach{ it.onNext(next) }
    }
}