package org.koolapp.reactive.support

import org.koolapp.reactive.*
import java.io.Closeable

object DefaultCloseable: Closeable {
    public override fun close() {
    }
}

/**
 * A task which iterates over an iterator invoking the [[Handler]]
 * until its complete
 */
class IteratorTask<T>(val iter: java.util.Iterator<T>, val handler: Handler<T>): Closeable, Runnable {
    var closed = false

    public override fun close() {
        closed = true
    }

    public override fun run(): Unit {
        try {
            for (element in iter) {
                handler.onNext(element)
                if (closed) break
            }
            handler.onComplete()
        } catch (e: Exception) {
            handler.onError(e)
        }
    }
}

