package test.koolapp.camel

import org.koolapp.camel.*

import org.junit.Test as test

class RouteBuilderTest {
    test fun createRoute() {
        val context = createCamelContext()

        // TODO the from("") and to("") breaks the compiler
        // as kotlin can't decide which one to invoke

        context.routes {
            route {
                from("seda:foo")!!.sendTo("seda:bar")
            }
        }
        context.use {
            println("Now has routes ${context}")
        }
    }
}
