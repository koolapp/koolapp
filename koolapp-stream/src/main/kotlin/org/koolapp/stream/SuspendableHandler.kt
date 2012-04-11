package org.koolapp.stream


/**
 * Handles asynchronous events from a stream where flow control may be required if the handler cannot process the next event.
 *
 * A [[Stream]] will invoke only one of these methods at a time from a single thread, so the handler does
 * not have to worry about concurrent access.
 *
 * The sequence of events is Open, Next*, (Complete|Error) so that there will always be an Open first
 * then zero to many Next events and finally either Complete or Error
 */
public abstract class SuspendableHandler<in T> {

    /**
     * Receives the [[Cursor]] when the stream is opened in case
     * the handler wishes to close the cursor itself
     */
    public abstract fun onOpen(cursor: SuspendableCursor): Unit

    /**
     * Receives the next value of a stream and attempts to process it, returning *true* if its processed
     * or *false* if it cannot be processed right now.
     *
     * If this method returns false then the stream should suspend itself
     */
    public abstract fun offerNext(next: T): Boolean

    /**
     * Marks a stream as completed
     */
    public abstract fun onComplete(): Unit

    /**
     * Marks a stream as completed with a failure
     */
    public abstract fun onError(e: Throwable): Unit
}
