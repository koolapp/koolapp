package org.koolapp.reactive.support

import org.koolapp.reactive.*
import java.io.Closeable

class DefaultCursor(): AbstractCursor() {
    protected override fun doClose() {
    }
}

/**
 * A task which iterates over an iterator invoking the [[Handler]]
 * until its complete
 */
class IteratorTask<T>(val iter: java.util.Iterator<T>, val handler: Handler<T>): AbstractCursor(), Runnable {
    public override fun run(): Unit {
        try {
            for (element in iter) {
                handler.onNext(element)
                if (isClosed()) break
            }
            handler.onComplete()
            close()
        } catch (e: Throwable) {
            handler.onError(e)
            close()
        }
    }
}

