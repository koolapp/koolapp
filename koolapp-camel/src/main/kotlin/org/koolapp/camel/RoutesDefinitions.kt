package org.koolapp.camel

import org.apache.camel.Endpoint
import org.apache.camel.Exchange
import org.apache.camel.model.*
import org.koolapp.camel.support.*

/**
* A builder API to help create a new [[RouteDefinition]] on a [[CamelContext]]
* using Camel's Java DSL
*/
inline fun RoutesDefinition.route(init: RouteDefinition.() -> Any): RouteDefinition {
    val definition = this.route()!!
    definition.init()
    return definition
}

/**
 * A builder API to help create a new [[RouteDefinition]] on a [[CamelContext]]
 * using Camel's Java DSL
 */
inline fun RoutesDefinition.from(uri: String, init: RouteDefinition.() -> Any): RouteDefinition {
    val definition = this.route()!!
    definition.from(uri)
    definition.init()
    return definition
}


/**
 * Sends the message to the given URI
 */
inline fun <T: ProcessorDefinition<T>> ProcessorDefinition<T>.sendTo(uri: String): T {
    return this.to(uri)!!
}

/**
 * Sends the message to the given endpoint
 */
inline fun <T: ProcessorDefinition<T>> ProcessorDefinition<T>.sendTo(endpoint: Endpoint): T {
    return this.to(endpoint)!!
}

/**
 * Performs a filter using the Kotlin function block as the predicate
 */
inline fun RouteDefinition.filter(predicate: (Exchange) -> Boolean, block: FilterDefinition.() -> Any): RouteDefinition {
//inline fun RouteDefinition.filter(predicate: Exchange.() -> Boolean, block: FilterDefinition.() -> Any): RouteDefinition {
    val predicateInstance = PredicateFunction(predicate)
    val filterBlock = filter(predicateInstance)!!
    filterBlock.block()
    return this
}
