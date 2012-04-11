package org.koolapp.stream

/**
 * A [[Cursor]] which can be suspended and resumed if a [[SuspendableHandler]]
 * needs some flow control.
 */
public trait SuspendableCursor : Cursor {

//    /**
//     * Returns true if this cursor is currently suspended
//     * due to flow control
//     */
//    public fun isSuspended(): Boolean
//
//    /**
//     * Suspends this cursor which should prevent it raising more events until
//     * the [[resume()]] function is called
//     */
//    public fun suspend(): Unit

    /**
     * Resumes this cursor to continue raising events again after its been suspended
     */
    public fun resume(): Unit
}