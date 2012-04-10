package org.koolapp.stream.support

import org.koolapp.stream.*
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Useful base class for [[Handler]] to avoid having to implement [[onComplete()]] or [[onError()]]
 */
abstract class AbstractHandler<T> : Handler<T>(), Closeable {
    private val closedFlag = AtomicBoolean(false)

    public override fun close() {
        if (closedFlag.compareAndSet(false, true)) {
            cursor?.close()
            doClose()
        }
    }

    /**
     * Returns true if this object is closed
     */
    public fun isClosed(): Boolean = closedFlag.get()


    var cursor: Cursor? = null

    public override fun onOpen(cursor: Cursor) {
        $cursor = cursor
    }

    public override fun onComplete() {
        close()
    }

    public override fun onError(e: Throwable) {
        close()
    }

    /**
     * Implementations can override this to implement custom closing logic
     */
    protected open fun doClose(): Unit {
    }
}

/**
 * Allows a function to be converted into an [[Handler]] so we can use a simple function to consume events
 */
class FunctionHandler<T>(val fn: (T) -> Unit): AbstractHandler<T>() {

    public override fun onNext(next: T) {
        if (fn != null) {
            (fn)(next)
        }
    }
}

/**
 * A [[Handler]] which filters elements in the stream
 */
class FilterHandler<T>(delegate: Handler<T>, val predicate: (T) -> Boolean): DelegateHandler<T,T>(delegate) {

    public override fun onNext(next: T) {
        if ((predicate)(next)) {
            delegate.onNext(next)
        }
    }
}


/**
 * A [[Handler]] which processes elements in the stream until the predicate is false then the underlying stream is closed
 */
class TakeWhileHandler<T>(var delegate: Handler<T>, val includeNonMatching: Boolean, val predicate: (T) -> Boolean): AbstractHandler<T>() {

    public override fun onNext(next: T) {
        val matches = (predicate)(next)
        if (matches || includeNonMatching) {
            delegate.onNext(next)
        }
        if (!matches) {
            close()
        }
    }

    protected override fun doClose() {
        delegate.onComplete()
        super.doClose()
    }
}
