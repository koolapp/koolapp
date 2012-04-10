package test.koolapp.stream.math

import kotlin.test.*

import org.koolapp.stream.*
import org.koolapp.stream.math.*

import org.junit.Test as test
import java.util.Queue

class WindowTest {

    test fun streamWithWindow() {
        var list = arrayList<Double>()
        for (i in 0..100) {
            list += i * 1.1
        }

        // TODO compile error if you miss out this type expression
        val window: Stream<Queue<Double>> = list.toStream().window(4)

        window.take(9).map{ it.variance() }.open { println("Has $it") }

        Thread.sleep(2000)
    }
}