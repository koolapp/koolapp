package org.koolapp.stream.support

import org.koolapp.stream.*
import java.io.Closeable
import java.util.concurrent.Executor
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.ScheduledFuture
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue


/**
 * Creates an [[Stream]] which puts each event into a [[Queue]] of a fixed size
 */
class WindowStream<T>(val delegate: Stream<T>, val size: Int) : Stream<Queue<T>>() {

    public override fun open(handler: Handler<Queue<T>>): Cursor {
        val newHandler: Handler<T> = WindowHandler(handler, size)
        val cursor = delegate.open(newHandler)
        newHandler.onOpen(cursor)
        return cursor
    }
}


/**
* Creates an [[Stream]] which puts the events into a moving window
*/
class WindowHandler<T>(delegate: Handler<Queue<T>>, val size: Int, val queue: Queue<T> = ConcurrentLinkedQueue<T>()): DelegateHandler<T,Queue<T>>(delegate) {

    public override fun onNext(next: T) {
        while (queue.size() >= size) {
            queue.remove()
        }
        queue.add(next)
        delegate.onNext(queue)
    }
}
