package test.koolapp.stream.math

import kotlin.test.*

import org.koolapp.stream.*
import org.koolapp.stream.math.*

import org.junit.Test as test
import java.util.List

class CalculationsOnBeansTest {

    class Trade(val symbol: String, val price: Double, val amount: Double) {
        fun toString() = "Trade($symbol, $price, $amount)"
    }

    test fun calculationsOnBeans() {
        val trades = arrayList(Trade("AAPL", 630.0, 100.0), Trade("GOOG", 625.0, 50.0), Trade("MSFT", 22.0, 5.0))

        val maxPrice = trades.max{ it.price }
        val amountVariance = trades.variance{ it.amount }

        println("maxprice = $maxPrice, amountVariance = $amountVariance")
        assertEquals(630.0, maxPrice)
        assertTrue(amountVariance > 0.0)
    }
}