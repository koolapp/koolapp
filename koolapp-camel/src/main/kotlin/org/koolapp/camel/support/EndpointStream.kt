package org.koolapp.camel.support

import org.apache.camel.Consumer
import org.apache.camel.Endpoint
import org.apache.camel.Exchange
import org.apache.camel.Processor as ExchangeProcessor
import org.koolapp.stream.Cursor
import org.koolapp.stream.Handler
import org.koolapp.stream.Stream
import org.koolapp.stream.support.*

/**
 * A [[Stream]] which consumes messages on a Camel [[Endpoint]]
 */
public class EndpointStream<T>(val endpoint: Endpoint, val klass: Class<T>): Stream<T>() {
    fun toString() = "EndpointStream($endpoint)"

    public override fun open(handler: Handler<T>): Cursor {
        val processor = if (klass.isAssignableFrom(javaClass<Exchange>())) {
            HandlerProcessor(handler, klass) {
                it as T
            }
        } else {
            HandlerProcessor(handler, klass) {
                it.getIn<T>(klass) as T
            }
        }
        val consumer = endpoint.createConsumer(processor)!!
        val cursor = ConsumerCursor(consumer, handler)
        handler.onOpen(cursor)
        consumer.start()
        return cursor
    }
}

public class HandlerProcessor<T>(val handler: Handler<T>, val klass: Class<T>, val fn: (Exchange) -> T): ExchangeProcessor {
    fun toString() = "HandlerProcessor($handler, $klass)"

    public override fun process(exchange: Exchange?): Unit {
        if (exchange != null) {
            val message = (fn)(exchange)
            if (message != null) {
                handler.onNext(message)
            }

        }
    }
}

/**
 * Represents a [[Cursor]] on a Camel [[Consumer]]
 *
 * The consumer is registered after this object is constructed
 * so that we can pass the cursor into the [[Handler]]'s Open before
 * the consumer is created to avoid it arriving after a Next
 */
public class ConsumerCursor(val consumer: Consumer, val handler: Handler<*>): AbstractCursor() {
    fun toString() = "ConsumerCursor($consumer, $handler)"

    public override fun doClose() {
        consumer.stop()
        handler.onComplete()
    }
}
