package org.koolapp.stream.support

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * A simple concurrent container API which is easy to implement using various strategies
 */
trait ConcurrentContainer<T> {
    /**
     * Process each item in the container
     */
    fun forEach(fn: (T) -> Unit): Unit

    /**
     * Adds an item to the collection
     */
    fun add(element: T): Unit

    /**
     * Removes an item from the collection
     */
    fun remove(element: T): Unit
}

class DefaultConcurrentContainer<T> : ConcurrentContainer<T> {
    val list = ConcurrentLinkedQueue<T>()

    override fun forEach(fn: (T) -> Unit) {
        for (element in list) {
            if (element != null) {
                (fn)(element)
            }
        }
    }

    override fun add(element: T) {
        list.add(element)
    }

    override fun remove(element: T) {
        list.remove(element)
    }

}