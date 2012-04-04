package org.koolapp.reactive


/**
 * Handles asynchronous events from a stream
 */
trait Handler<in T> {

    /**
     * Receives the next value of a stream
     */
    fun onNext(next: T): Unit

    /**
     * Marks a stream as completed
     */
    fun onComplete(): Unit

    /**
     * Marks a stream as completed with a failure
     */
    fun onError(e: Exception): Unit
}
