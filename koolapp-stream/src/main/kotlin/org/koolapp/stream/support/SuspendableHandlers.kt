package org.koolapp.stream.support

import java.util.ArrayDeque
import java.util.Queue
import java.util.concurrent.atomic.AtomicBoolean
import org.koolapp.stream.*
import java.io.Closeable

/**
 * Adapts a [[SuspendableHandler]] to work with a regular [[Stream]] which works with a [[Handler]]
 * by buffering up any events which could not be offered so they can be retried before the next event is
 * offered
 */
open class SuspendableHandlerAdapter<T>(val delegate: SuspendableHandler<T>): Handler<T>() {
    val buffer: Queue<T> = ArrayDeque<T>()
    var suspendableCursor: SuspendableCursor? = null

    public override fun onOpen(cursor: Cursor) {
        val newCursor = cursor.toSuspendableCursor()
        suspendableCursor = newCursor
        delegate.onOpen(newCursor)
    }

    public override fun onComplete() {
        delegate.onComplete()
    }

    public override fun onError(e: Throwable) {
        delegate.onError(e)
    }

    public override fun onNext(next: T) {
        while (buffer.notEmpty()) {
            val head = buffer.peek()
            if (head == null || delegate.offerNext(head)) {
                buffer.remove()
            } else {
                buffer.add(next)
                onOfferFailed()
                return
            }
        }
        val result = delegate.offerNext(next)
        if (!result) {
            buffer.add(next)
            onOfferFailed()
        }
    }

    /**
     * We cannot suspend the cursor as we have no way to know
     * when to resume it again but a derived class may choose to do so
     */
    protected open fun onOfferFailed(): Unit {
    }
}

class SuspendableCursorAdapter(val delegate: Cursor): AbstractCursor(), SuspendableCursor {
    val suspended = AtomicBoolean(false)

    public override fun isSuspended(): Boolean {
        return suspended.get()
    }

    public override fun resume() {
        suspended.set(false)
    }

    public override fun suspend() {
        suspended.set(true)
    }

    protected override fun doClose() {
        delegate.close()
    }
}

/**
 * Useful base class for [[Handler]] to avoid having to implement [[onComplete()]] or [[onError()]]
 */
abstract class AbstractSuspendableHandler<T> : SuspendableHandler<T>(), Closeable {
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


    var cursor: SuspendableCursor? = null

    public override fun onOpen(cursor: SuspendableCursor) {
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
 * Allows a function to be converted into an [[SuspendableHandler]] so we can use a simple function to consume events
 */
class FunctionSuspendableHandler<T>(val fn: (T) -> Boolean) : AbstractSuspendableHandler<T>() {
    public override fun offerNext(next: T): Boolean {
        return (fn)(next)
    }
}