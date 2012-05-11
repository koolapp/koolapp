package org.koolapp.camel

import org.apache.camel.Endpoint
import org.apache.camel.Exchange
import org.apache.camel.model.*
import org.koolapp.camel.support.*
import java.util.List

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
inline fun <T: ProcessorDefinition<T>> T.sendTo(uri: String): T {
    return this.to(uri)!!
}

/**
 * Sends the message to the given endpoint
 */
inline fun <T: ProcessorDefinition<T>> T.sendTo(endpoint: Endpoint): T {
    return this.to(endpoint)!!
}

// TODO the sendTo methods on FilterDefinition should not really be required
// they are workarounds from compiler glitches

/**
 * Sends the message to the given URI
 */
inline fun FilterDefinition.sendTo(uri: String): FilterDefinition {
    to(uri)
    return this
}

/**
 * Sends the message to the given endpoint
 */
inline fun FilterDefinition.sendTo(endpoint: Endpoint): FilterDefinition {
    to(endpoint)
    return this
}
/**
 * Performs a filter using the function block as the predicate
 */
inline fun <T: ProcessorDefinition<T>> T.filter(predicate: Exchange.() -> Boolean, block: FilterDefinition.() -> Any): T {
    val predicateInstance = PredicateFunction(predicate)
    val filterBlock: FilterDefinition = filter(predicateInstance)!!
    filterBlock.block()
    return this
}

/**
 * Performs a filter using the function block as the predicate, then calling *then* on the result object will
 * pass the action block.
 */
inline fun <T: ProcessorDefinition<T>> T.filter(predicate: Exchange.() -> Boolean): ThenDefinition<T> {
    val predicateInstance = PredicateFunction(predicate)
    val filterBlock = filter(predicateInstance)!!
    return ThenDefinition<T>(filterBlock)
}

/**
 * Performs a content based router
 */
inline fun <T: ProcessorDefinition<T>> T.choice(block: ChoiceDefinition.() -> Any?): T {
    val choiceBlock = choice()!!
    choiceBlock.block()
    return this
}

/**
 * Evaluates the nested body if this predicate matches
 */
inline fun ChoiceDefinition.filter(predicate: Exchange.() -> Boolean): ThenDefinition<ChoiceDefinition> {
    val predicateInstance = PredicateFunction(predicate)
    this.`when`(predicateInstance)

    // lets get the last when output
    val outputs: List<Any> = this.getOutputs()!! as List<Any>
    val whenDefinition = outputs.tail
    if (whenDefinition is WhenDefinition) {
        return ThenDefinition<ChoiceDefinition>(whenDefinition)
    } else {
        throw IllegalStateException("Could not find WhenDefinition in ChoiceDefinition, found " + whenDefinition)
    }
}

/**
 * Adds an otherwise section
 */
inline fun ChoiceDefinition.otherwise(block: ChoiceDefinition.() -> Any?): ChoiceDefinition {
    this.otherwise()
    this.block()
    end()

    // TODO should we be returning the parent node?
    return this
}
