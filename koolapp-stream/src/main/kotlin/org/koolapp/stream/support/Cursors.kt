package org.koolapp.stream.support

import org.koolapp.stream.*
import java.io.Closeable
import java.util.TimerTask
import java.util.concurrent.Future

public open class DefaultCursor(): AbstractCursor() {
    protected override fun doClose() {
    }
}

/**
 * A task which iterates over an iterator invoking the [[Handler]]
 * until its complete
 */
public open class IteratorTask<T>(val iter: java.util.Iterator<T>, val handler: Handler<T>): DefaultCursor(), Runnable {
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

/**
 * A [[Cursor]] for closing an underlying [[TimerTask]]
 */
public class TimerTaskCursor(val task: TimerTask, val handler: Handler<*>) : AbstractCursor() {

    public override fun doClose() {
        task.cancel()
        handler.onComplete()
    }
}

/**
 * A [[Cursor]] for closing an underlying [[Future]]
 */
public class FutureCursor( val handler: Handler<*>, val mayInterruptIfRunningOnClose: Boolean = true) : AbstractCursor() {

    /**
     * Allows the future to be specified after this cursor has been created so that the cursor can be passed into the
     * Open event before the Future has been created to avoid timing issues where a Future may complete before a stream
     * is opened
     */
    var future: Future<*>? = null

    public override fun doClose() {
        future?.cancel(mayInterruptIfRunningOnClose)
        handler.onComplete()
    }
}