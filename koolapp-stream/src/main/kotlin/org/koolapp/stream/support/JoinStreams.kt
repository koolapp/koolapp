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
abstract class JoinStream<A,B,T>(val streamA: Stream<A>, val streamB: Stream<B>): Stream<T>() {

    public override fun open(handler: Handler<T>): Cursor {
        val newHandler = createHandler(handler)
        val cursor = newHandler.open()
        newHandler.onOpen(cursor)
        return cursor
    }

    protected abstract fun createHandler(handler: Handler<T>): JoinHandlerSupport<A,B,T>
}


/**
 * A useful base class for combining two [[Stream]] instances together into a new stream
 */
abstract class JoinHandlerSupport<A,B,T>(val streamA: Stream<A>, val streamB: Stream<B>, delegate: Handler<T>): DelegateHandler<T,T>(delegate) {

    val handlerA = FunctionHandler<A>{
        onNextA(it)
    }

    val handlerB = FunctionHandler<B>{
        onNextB(it)
    }

    public override fun onNext(next: T) {
        delegate.onNext(next)
    }

    /**
     * Processes the incoming event on [[streamA]]
     */
    protected abstract fun onNextA(a: A): Unit

    /**
     * Processes the incoming event on [[streamB]]
     */
    protected abstract fun onNextB(b: B): Unit

    /**
     * Open on the underying streams and return a composite cursor
     */
    public fun open(): Cursor {
        val cursorA = streamA.open(handlerA)
        val cursorB = streamB.open(handlerB)
        return CompositeCursor(arrayList(cursorA, cursorB))
    }
}

/**
 * Creates an [[Stream]] which emits events of type [[#(A,B)]] when *stream1* has had an event and *stream2* has had an event
 */
class FollowedByStream<A,B>(streamA: Stream<A>, streamB: Stream<B>) : JoinStream<A,B,#(A,B)>(streamA, streamB) {

    protected override fun createHandler(handler: Handler<#(A, B)>): JoinHandlerSupport<A, B, #(A, B)> {
        return FollowedByHandler<A,B>(streamA, streamB, handler)
    }
}

/**
 * Creates an [[Stream]] of an events of type [[#(A,B)]] when an event occurs on *streamA* followed by an event on *streamB*.
 * We filter out consecutive events on *streamA* or events on *streamB* before there is an event on *streamA*.
 */
open class FollowedByHandler<A,B>(streamA: Stream<A>, streamB: Stream<B>, delegate: Handler<#(A,B)>): JoinHandlerSupport<A, B, #(A,B)>(streamA, streamB, delegate) {
    open var valueA: A? = null
    open var valueB: B? = null
    val lock = ReentrantLock()

    protected override fun onNextA(a: A): Unit {
        valueA = a
        checkIfComplete()
    }

    protected override fun onNextB(b: B): Unit {
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
}
