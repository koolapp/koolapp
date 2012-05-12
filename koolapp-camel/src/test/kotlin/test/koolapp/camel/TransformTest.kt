package test.koolapp.camel

import org.koolapp.camel.*

import org.junit.Test as test
import org.apache.camel.component.mock.MockEndpoint

class TransformTest {
    test fun createRoute() {
        camel {
            val result = mockEndpoint("mock:result")
            routes {
                from("seda:foo") {
                    transform { "Hello ${bodyString()}" }.sendTo(result)
                }
            }
            result.expectedBodiesReceived("Hello world!")

            val producer = producerTemplate()
            producer.sendBody("seda:foo", "world!")

            result.assertIsSatisfied()

            for (exchange in result.getReceivedExchanges()) {
                println("Found message ${exchange?.getIn()}")
            }
        }
    }
}
