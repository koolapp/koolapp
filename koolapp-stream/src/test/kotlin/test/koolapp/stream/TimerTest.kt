package test.koolapp.stream

import org.koolapp.stream.*
import java.util.Timer

import kotlin.test.*
import org.junit.Test as test

class TimerTest {

    test fun subject() {
        var results = arrayList<Long>()

        val timer = Timer()
        val c1 = timer.intervalStream(1000).take(5).open {
            println("Timer fired at $it")
            results += it
        }

        Thread.sleep(10000)
        println("Interval Stream generated: results: $results")
    }


}