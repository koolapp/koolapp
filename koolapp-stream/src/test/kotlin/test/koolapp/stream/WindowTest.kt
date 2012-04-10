package test.koolapp.stream

import kotlin.test.*

import org.koolapp.stream.*

import org.junit.Test as test

import java.util.*

class WindowTest {

    test fun streamWithWindow() {
        var list = arrayList<Double>()
        for (i in 0..100) {
            list += i * 1.1
        }

        // TODO compile error if you miss out this type
        val window: Stream<Queue<Double>> = list.toStream().window(4)

        window.take(9).open { (q: Queue<Double>) -> println("Has queue of $q") }
        Thread.sleep(2000)
    }
}