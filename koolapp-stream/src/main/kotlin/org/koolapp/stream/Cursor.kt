package org.koolapp.stream

import org.koolapp.stream.support.SuspendableCursorAdapter
import java.io.Closeable

/**
 * Represents the processing of a [[Stream]] by a [[Handler]]
 * which can be closed via the [[Closeable]] interface
 */
public trait Cursor: Closeable {
    fun isClosed(): Boolean
}

/**
 * Converts this [[Cursor]] to a [[SuspendableCursor]] if it not already
 */
inline fun Cursor.toSuspendableCursor(): SuspendableCursor {
    return if (this is SuspendableCursor) {
        this
    } else {
        SuspendableCursorAdapter(this)
    }
}