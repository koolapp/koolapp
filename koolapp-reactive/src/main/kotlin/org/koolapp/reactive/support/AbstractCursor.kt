package org.koolapp.reactive.support

import org.koolapp.reactive.*

import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A useful base class for implementing a [[Cursor]]
 */
abstract class AbstractCursor: Cursor {
    private val closedFlag = AtomicBoolean(false)

    /**
     * Allows a closeable to be added which will be closed as part of the same lifecycle
     */
    public var closeable: Closeable? = null

    public override fun close() {
        if (closedFlag.compareAndSet(false, true)) {
            doClose()
        }
    }

    /**
     * Returns true if this object is closed
     */
    public override fun isClosed(): Boolean = closedFlag.get()


    /**
     * Implementations must implement this method to perform the actual close logic
     */
    protected open fun doClose() {
        closeable?.close()
    }
}