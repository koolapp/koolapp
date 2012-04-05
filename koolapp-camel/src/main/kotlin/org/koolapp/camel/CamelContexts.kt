package org.koolapp.camel

import org.koolapp.stream.Stream
import org.koolapp.stream.support.*
import org.apache.camel.CamelContext
import org.apache.camel.Endpoint
import org.apache.camel.util.CamelContextHelper

/**
 * Looks up the given endpoint in the [[CamelContext]] throwing an exception if its not available
 */
fun CamelContext.requireEndpoint(uri: String): Endpoint = CamelContextHelper.getMandatoryEndpoint(this, uri)!!

/**
 * Creates a stream of events
 */
fun <T> CamelContext.endpointStream(uri: String): Stream<T> {
    val endpoint = requireEndpoint(uri)
    throw UnsupportedOperationException("TODO")
}