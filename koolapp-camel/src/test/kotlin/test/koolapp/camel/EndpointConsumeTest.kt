package test.koolapp.camel

import org.koolapp.stream.*
import org.koolapp.camel.*

import org.apache.camel.*
import org.apache.camel.CamelContext
import org.apache.camel.Consumer
import org.apache.camel.Endpoint
import org.apache.camel.Processor as ExchangeProcessor
import org.apache.camel.spi.*
import org.apache.camel.impl.*
import org.apache.camel.util.CamelContextHelper

import org.koolapp.stream.Stream
import org.koolapp.stream.support.*
import org.koolapp.stream.Handler
import org.koolapp.stream.Cursor
import javax.annotation.processing.Processor
import org.apache.camel.Exchange

import org.junit.Test as test
import kotlin.test.*

class EndpointConsumeTest {

    test fun consumeExchangeFromEndpoint() {
        val context = createCamelContext()
        context.use {
            val stream = context.endpointStream<Exchange>("timer://foo?fixedRate=true&period=1000", javaClass<Exchange>)
            val cursor = stream.take(4).open{ println("Stream Exchange handler got $it of type ${it.javaClass} with properties ${it.getProperties()}") }

            Thread.sleep(6000)
            assertTrue(cursor.isClosed())
        }
    }

    test fun consumeMessageFromEndpoint() {
        val context = createCamelContext()
        context.use {
            val stream = context.endpointStream<Message>("timer://foo?fixedRate=true&period=1000", javaClass<Message>)
            val cursor = stream.take(4).open{ println("Stream Message handler got $it of type ${it.javaClass} with headers ${it.getHeaders()}") }

            Thread.sleep(6000)
            assertTrue(cursor.isClosed())
        }
    }

    test fun consumeStringFromEndpoint() {
        val context = createCamelContext()
        context.use {
            val stream = context.endpointStream<Message>("timer://foo?fixedRate=true&period=1000", javaClass<Message>)
            val cursor = stream.map{ it.getHeader<String>("firedTime", javaClass<String>()) }.take(4).open{
                println("Stream String handler got $it of type ${it.javaClass}")
            }

            Thread.sleep(6000)
            assertTrue(cursor.isClosed())
        }
    }
}