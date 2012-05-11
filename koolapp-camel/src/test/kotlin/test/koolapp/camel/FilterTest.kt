package test.koolapp.camel

import org.koolapp.camel.*

import org.junit.Test as test
import org.apache.camel.component.mock.MockEndpoint

class FilterTest {
    test fun createRoute() {
        val context = createCamelContext()

        context.use {
            val result = mockEndpoint("mock:result")
            routes {
                from("seda:foo") {
                    filter({ bodyString().contains("big") }, {
                        sendTo(result)
                    })
                }
            }
            println("Now has routes ${context}")

            result.expectedBodiesReceived("big1")

            val producer = producerTemplate()
            for (body in arrayList("small1", "big1", "small2")) {
                producer.sendBody("seda:foo", body)
            }

            result.assertIsSatisfied()

            for (exchange in result.getReceivedExchanges()) {
                println("Found message ${exchange?.getIn()}")
            }
        }
    }
}
