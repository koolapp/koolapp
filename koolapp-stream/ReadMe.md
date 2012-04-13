## Kool Streams

**Kool Streams** is a simple framework for working with aynchronous events and collections. Kool Streams are inspired by a combination of the [Reactive Extensions (Rx)](http://msdn.microsoft.com/en-us/data/gg577609),
[Iteratees](http://okmij.org/ftp/Streams.html) and various other similar approaches to dealing with concurrency.

### Why Kool Stream?

Kool Streams provide a form of asynchronous collection or **event Stream** that can be
[composed and processed](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/CollectionTest.kt#L15)
like regular [collections](http://jetbrains.github.com/kotlin/versions/snapshot/apidocs/kotlin/java/util/Collection-extensions.html)
using the same kind of combinator API folks are familiar with (filter(), flatMap(), map(), fold() etc)
but done asynchronously to deal with time delay, use threads efficiently and avoid blocking.

### Examples

Create event streams from various things:

* [java.util.Collection](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/CollectionTest.kt#L10)
* bean event listeners (TODO :)
* [java.util.Timer](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/TimerTest.kt#L14)
* [java.util.concurrent.ScheduledExecutorService](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/ScheduledExecutorServiceTest.kt#L17)
* [SimpleStream](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/SimpleStreamTest.kt#L15)
* [Apache Camel Endpoints](https://github.com/koolapp/koolapp/blob/master/koolapp-camel/src/test/kotlin/test/koolapp/camel/EndpointConsumeTest.kt#L27)

Combine streams with Collection-style combinators

* [filter and map values](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/CollectionTest.kt#L15) via filter() and map()
* [filter distinct values](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/DistinctTest.kt#L14) via distinct()

Using windows of events for *complex event processing* types of things

* [create a moving fixed window of events](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/WindowTest.kt#L21) via window(size)
* [create a moving time window of events](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/TimeWindowTest.kt#L22) via timeWindow(millis)
* [group events in a window](https://github.com/koolapp/koolapp/blob/master/koolapp-math/src/test/kotlin/test/koolapp/math/GroupByTest.kt#L11) using groupBy(keyFunction)