package org.koolapp.camel

import org.koolapp.stream.Stream
import org.koolapp.stream.support.*
import org.koolapp.camel.support.*
import org.apache.camel.CamelContext
import org.apache.camel.Endpoint
import org.apache.camel.util.CamelContextHelper
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.Exchange
import org.apache.camel.Message

/**
 * Looks up the given endpoint in the [[CamelContext]] throwing an exception if its not available
 */
inline fun CamelContext.endpoint(uri: String): Endpoint = CamelContextHelper.getMandatoryEndpoint(this, uri)!!

/**
 * Starts the given [[CamelContext]], processes the block and then stops it at the end
 */
inline fun <T> CamelContext.use(block: (CamelContext) -> T): T {
    var closed = false
    try {
        this.start()
        return block(this)
    } catch (e: Exception) {
        closed = true
        try {
            this.stop()
        } catch (closeException: Exception) {
            // eat the closeException as we are already throwing the original cause
            // and we don't want to mask the real exception

            // TODO on Java 7 we should call
            // e.addSuppressed(closeException)
            // to work like try-with-resources
            // http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html#suppressed-exceptions
        }
        throw e
    } finally {
        if (!closed) {
            this.stop()
        }
    }
}

/**
 * Helper method to create a new [[CamelContext]]
 */
inline fun createCamelContext(): CamelContext {
    return DefaultCamelContext()
}


/**
 * Creates a stream of [[Message]] objects by consuming from the given [[Endpoint]] URI
 */
inline fun Endpoint.toStream(): Stream<Message> {
    return toStream{ it.getIn()!! }
}

/**
 * Creates a stream of events by consuming messages from the given [[Endpoint]] URI and applying the given function on each Exchange
 */
inline fun <T> Endpoint.toStream(fn: (Exchange) -> T): Stream<T> {
    return EndpointStream(this, fn)
}

/**
 * Creates a stream of [[Exchange]] objects by consuming from the given [[Endpoint]] URI
 */
inline fun Endpoint.toExchangeStream(): Stream<Exchange> {
    return toStream{ it }
}

/**
 * Creates a stream of events by consuming messages from the given [[Endpoint]] of the given type
 */
// TODO is there a way to avoid explicit passing in of the class?
inline fun <T> Endpoint.toStreamOf(klass: Class<T>): Stream<T> {
    return if (klass.isAssignableFrom(javaClass<Exchange>())) {
        EndpointStream(this) {
            it as T
        }
    } else {
        EndpointStream(this) {
            it.getIn<T>(klass) as T
        }
    }
}
