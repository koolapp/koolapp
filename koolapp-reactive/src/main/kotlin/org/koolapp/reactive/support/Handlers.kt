package org.koolapp.reactive.support

import org.koolapp.reactive.*
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Useful base class for [[Handler]] to avoid having to implement [[onComplete()]] or [[onError()]]
 */
abstract class AbstractHandler<T> : Handler<T> {
    override fun onComplete() {
    }

    override fun onError(e: Throwable) {
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
abstract class DelegateHandler<T>(val delegate: Handler<T>) : Handler<T> {

    public override fun onComplete() {
        delegate.onComplete()
    }

    public override fun onError(e: Throwable) {
        delegate.onError(e)
    }

    public override fun onNext(next: T) {
        delegate.onNext(next)
    }
}

/**
 * A [[Handler]] which filters elements in the stream
 */
class FilterHandler<T>(delegate: Handler<T>, val predicate: (T) -> Boolean): DelegateHandler<T>(delegate) {

    public override fun onNext(next: T) {
        if ((predicate)(next)) {
            delegate.onNext(next)
        }
    }
}

/**
 * A [[Handler]] which processes elements in the stream until the predicate is false then the underlying stream is closed
 */
class TakeWhileHandler<T>(var delegate: Handler<T>, val predicate: (T) -> Boolean): AbstractCursor(), Handler<T> {

    public override fun onNext(next: T) {
        if ((predicate)(next)) {
            delegate.onNext(next)
        } else {
            close()
        }
    }

    public override fun onComplete() {
        close()
    }

    public override fun onError(e: Throwable) {
        close()
    }

    protected override fun doClose() {
        delegate.onComplete()
        super<AbstractCursor>.doClose()
    }
}

/**
 * A [[Handler]] which filters elements in the stream
 */
class MapHandler<T, R>(val delegate: Handler<R>, val transform: (T) -> R): Handler<T> {

    public override fun onNext(next: T) {
        val result = (transform)(next)
        delegate.onNext(result)
    }

    override fun onComplete() {
        delegate.onComplete()
    }

    override fun onError(e: Throwable) {
        delegate.onError(e)
    }
}