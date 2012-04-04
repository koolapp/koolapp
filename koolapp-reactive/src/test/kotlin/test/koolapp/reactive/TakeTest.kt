package test.koolapp.reactive

import kotlin.test.*

import org.koolapp.reactive.*
import org.koolapp.reactive.support.*

import org.junit.Test as test

class TakeTest {

    test fun takeFromStream() {
        var results = arrayList<String>()
        val stream = SimpleStream<String>()

        val c1 = stream.take(1).open{ results += it }

        stream.onNext("foo")
        stream.onNext("bar")

        if (c1 is AbstractCloseable) {
            assertEquals(true, c1.isClosed())
        }

        println("Stream generated: results1: $results")
        assertEquals(arrayList("foo"), results)
    }


    test fun takeWhileFromStream() {
        var results = arrayList<String>()
        val stream = SimpleStream<String>()

        val c1 = stream.takeWhile{ it.startsWith("f") }.open{ results += it }

        stream.onNext("foo")
        stream.onNext("foo2")
        stream.onNext("bar")

        if (c1 is AbstractCloseable) {
            assertEquals(true, c1.isClosed())
        }

        println("Stream generated: results1: $results")
        assertEquals(arrayList("foo", "foo2"), results)

    }

}