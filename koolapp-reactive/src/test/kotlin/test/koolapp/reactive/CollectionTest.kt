package test.koolapp.reactive

import org.koolapp.reactive.*

import org.junit.Test as test

class CollectionTest {

    test fun collectionAsStream() {
        val list = arrayList("foo", "bar")
        val stream = list.toStream()
        val closeable = stream.subscribe{ println("String stream: $it") }
    }

    test fun collectionStreamFilterAndMap() {
        val list = arrayList("foo", "f", "bar")
        val stream = list.toStream().filter{ it.startsWith("f") }.map{ it.size }
        val closeable = stream.subscribe{ println("Number stream: $it") }
    }


}