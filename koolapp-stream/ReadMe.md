## Kool Streams

**Kool Streams** is a simple framework for working with aynchronous events and collections. Kool Streams are inspired by a combination of the [Reactive Extensions (Rx)](http://msdn.microsoft.com/en-us/data/gg577609),
[Iteratees](http://okmij.org/ftp/Streams.html) and various other similar approaches to dealing with concurrency.

### Why Kool Stream?

Kool Streams provide a form of asynchronous collection or **Event Stream** that can be
[composed and processed](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/CollectionTest.kt#L15)
like regular [collections](http://jetbrains.github.com/kotlin/versions/snapshot/apidocs/kotlin/java/util/Collection-extensions.html)
using the same kind of combinator API folks are familiar with (filter(), flatMap(), map(), fold() etc)
but done asynchronously to deal with time delay, network communication, to use threads efficiently and avoid blocking.

### API Overview

* [Stream<T>](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Stream.html) represents a stream of asynchronous events.
Its like an asynchronous collection, where events are pushed into a
[Handler<T>](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Handler.html) rather than pulled via an iterator.
* [Handler<T>](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Handler.html) is used to process the events from a stream, through you can just pass a function to handle each next event instead

A [Stream<T>](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Stream.html) is then opened via the **open()** method either passing in
a handler in [open(Handler<T>)](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Stream.html#open(org.koolapp.stream.Handler)) or by passing in
a function to handle even next event via [open(fn: (T) -> Unit)](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Stream.html#open(jet.Function1)).

When you **open()** a [Stream<T>](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Stream.html) you get back a
[Cursor](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Cursor.html) which can be used to close the stream.

### Stream contract

A [Stream<T>](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Stream.html) must only invoke a
[Handler<T>](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Handler.html) from one thread at once; so a
handler does not need to worry about being thread safe.

The lifecycle of events from a [Handler<T>](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Handler.html) perspective is a stream will invoke

* [onOpen(Cursor)](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Handler.html#onOpen(org.koolapp.stream.Cursor)) once before any other events
* [onNext(T)](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Handler.html#onNext(T)) zero to many times for each event on the stream after opening
* [onError(Throwable)](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Handler.html#onError(jet.Throwable)) on errors
* [onComplete()](http://koolapp.org/versions/snapshot/apidocs/org/koolapp/stream/Handler.html#onComplete()) when its completed

This means that a Handler can choose to close a stream if it knows it has finished processing it. For example if you can use
[take(n)](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/TakeTest.kt#L14) to limit the size of a stream.

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
* [take a specific amount of events](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/TakeTest.kt#L14) via take(n) or takeWhile(predicate)

Using windows of events for *complex event processing* types of things

* [create a moving fixed window of events](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/WindowTest.kt#L21) via window(size)
* [create a moving time window of events](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/TimeWindowTest.kt#L22) via timeWindow(millis)
* [group events in a window](https://github.com/koolapp/koolapp/blob/master/koolapp-math/src/test/kotlin/test/koolapp/math/GroupByTest.kt#L11) using groupBy(keyFunction)

Finally you can pipe the events from a Stream [to any Apache Camel Endpoint](https://github.com/koolapp/koolapp/blob/master/koolapp-camel/src/test/kotlin/test/koolapp/camel/EndpointProduceTest.kt#L33)
