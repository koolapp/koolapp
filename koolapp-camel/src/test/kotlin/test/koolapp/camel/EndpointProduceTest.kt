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

class EndpointProduceTest {

    test fun endpointExchangeStream() {
        val context = createCamelContext()
        context.use {

            val inStream = context.endpoint("timer://foo?fixedRate=true&period=1000").toExchangeStream()

            val resultsEndpoint = context.endpoint("seda:resultsEndpoint")
            inStream.sendTo(resultsEndpoint)

            val outStream = resultsEndpoint.toStream()
            val cursor = outStream.take(4).open{ println("handler consuming from $resultsEndpoint got $it of type ${it.javaClass}") }

            Thread.sleep(6000)
            assertTrue(cursor.isClosed())
        }
    }


}