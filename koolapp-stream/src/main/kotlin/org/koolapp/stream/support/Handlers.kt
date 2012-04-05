package org.koolapp.stream.support

import org.koolapp.stream.*
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Useful base class for [[Handler]] to avoid having to implement [[onComplete()]] or [[onError()]]
 */
abstract class AbstractHandler<T> : AbstractCursor(), Handler<T> {
    var cursor: Cursor? = null

    override fun onOpen(cursor: Cursor) {
        $cursor = cursor
    }

    public override fun onComplete() {
        close()
    }

    public override fun onError(e: Throwable) {
        close()
    }

    protected override fun doClose() {
        cursor?.close()
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
 * Useful base class which delegates to another [[Handler]]
 */
abstract class DelegateHandler<T,R>(val delegate: Handler<R>) : Handler<T> {

    override fun onOpen(cursor: Cursor) {
        delegate.onOpen(cursor)
    }

    public override fun onComplete() {
        delegate.onComplete()
    }

    public override fun onError(e: Throwable) {
        delegate.onError(e)
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
 * A [[Handler]] which filters elements in the stream
 */
class MapHandler<T, R>(delegate: Handler<R>, val transform: (T) -> R): DelegateHandler<T,R>(delegate) {

    public override fun onNext(next: T) {
        val result = (transform)(next)
        delegate.onNext(result)
    }
}

/**
 * A [[Handler]] which processes elements in the stream until the predicate is false then the underlying stream is closed
 */
class TakeWhileHandler<T>(var delegate: Handler<T>, val predicate: (T) -> Boolean): AbstractHandler<T>() {

    public override fun onNext(next: T) {
        if ((predicate)(next)) {
            delegate.onNext(next)
        } else {
            close()
        }
    }

    protected override fun doClose() {
        delegate.onComplete()
        super.doClose()
    }
}
