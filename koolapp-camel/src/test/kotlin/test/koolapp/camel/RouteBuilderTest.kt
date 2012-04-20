package test.koolapp.camel

import org.koolapp.camel.*

import org.junit.Test as test

class RouteBuilderTest {
    test fun createRoute() {
        val context = createCamelContext()

        context.routes {
            from("seda:foo") {
                sendTo("seda:bar")
            }
        }
        context.use {
            println("Now has routes ${context}")
        }

    }
}
