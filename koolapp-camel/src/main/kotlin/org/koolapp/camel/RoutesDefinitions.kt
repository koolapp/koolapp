package org.koolapp.camel


import org.koolapp.camel.support.*
import org.apache.camel.CamelContext
import org.apache.camel.Endpoint
import org.apache.camel.util.CamelContextHelper
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.model.*


/**
 * A builder API to help create a new [[RouteDefinition]] on a [[CamelContext]]
 * using Camel's Java DSL
 */
/*
*/
inline fun RoutesDefinition.route(init: RouteDefinition.() -> Any): RouteDefinition {
    val definition = this.route()!!
    definition.init()
    return definition
}
