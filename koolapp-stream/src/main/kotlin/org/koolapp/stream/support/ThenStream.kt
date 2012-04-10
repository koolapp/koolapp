package org.koolapp.stream.support

import kotlin.concurrent.*
import java.util.ArrayDeque
import java.util.ArrayList
import java.util.List
import java.util.Queue
import org.koolapp.stream.*
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.locks.ReentrantLock

/**
* Creates an [[Stream]] which emits a tuple of [[#(A,B)]] when *stream1* has had an event and *stream2* has had an event
*/
class ThenStream<A,B>(val streamA: Stream<A>, val streamB: Stream<B>) : Stream<#(A,B)>() {

    public override fun open(handler: Handler<#(A,B)>): Cursor {
        val newHandler = ThenHandler<A,B>(streamA, streamB, handler)
        val cursor = newHandler.open()
        newHandler.onOpen(cursor)
        return cursor
    }
}

/**
 * Creates an [[Stream]] which puts the events into a moving window
 */
open class ThenHandler<A,B>(val streamA: Stream<A>, val streamB: Stream<B>, delegate: Handler<#(A,B)>): DelegateHandler<#(A,B),#(A,B)>(delegate) {
    open var valueA: A? = null
    open var valueB: B? = null
    val lock = ReentrantLock()

    val handlerA = FunctionHandler<A>{
        onNextA(it)
    }

    val handlerB = FunctionHandler<B>{
        onNextB(it)
    }

    public override fun onNext(next: #(A, B)) {
        delegate.onNext(next)
    }

    protected open fun onNextA(a: A): Unit {
        valueA = a
        checkIfComplete()
    }

    protected open fun onNextB(b: B): Unit {
        valueB = b
        checkIfComplete()
    }

    protected open fun checkIfComplete(): Unit {
        lock.withLock {
            val a = valueA
            val b = valueB
            if (a != null && b != null) {
                valueA = null
                valueB = null
                val next = #(a!!, b!!)
                onNext(next)
            }
        }
    }

    /**
     * Open on the underying streams and return a composite cursor
     */
    public fun open(): Cursor {
        val cursorA = streamA.open(handlerA)
        val cursorB = streamB.open(handlerB)
        return CompositeCursor(arrayList(cursorA, cursorB))
    }
}
