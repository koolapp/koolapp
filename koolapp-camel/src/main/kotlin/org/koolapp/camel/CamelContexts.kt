package org.koolapp.camel

import org.koolapp.stream.*
import org.koolapp.stream.support.*
import org.koolapp.camel.support.*
import org.apache.camel.CamelContext
import org.apache.camel.Endpoint
import org.apache.camel.util.CamelContextHelper
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.model.ModelCamelContext

// TODO cannot import this class as it blows up the compiler
//import org.apache.camel.model.RouteDefinition

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
 * Helper method to create a new [[ModelCamelContext]]
 */
inline fun createCamelContext(): ModelCamelContext {
    return DefaultCamelContext()
}


/**
 * A builder to add some route builders to the [[ModelCamelContext]]
 */
/*
//inline fun ModelCamelContext.routes(init: RoutesDefinition.() -> Unit): RoutesDefinition {
inline fun CamelContext.routes(init: () -> Unit): RoutesDefinition {
    val definition = RoutesDefinition()
    routes.init()
    addRouteDefinitions(routes.getRoutes())
    return definition
}
*/
