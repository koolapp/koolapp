## Kool Streams

**Kool Streams** is a simple framework for working with aynchronous events and collections. Kool Streams are inspired by a combination of the [Reactive Extensions (Rx)](http://msdn.microsoft.com/en-us/data/gg577609),
[Iteratees](http://okmij.org/ftp/Streams.html) and various other similar approaches to dealing with concurrency.

The basic idea is to provide support for asynchronous collections or event **Streams** that can be
[composed and processed](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/src/test/kotlin/test/koolapp/stream/CollectionTest.kt#L15)
like regular [collections](http://jetbrains.github.com/kotlin/versions/snapshot/apidocs/kotlin/java/util/Collection-extensions.html)
but done asynchronously to deal with time delay, use threads efficiently and avoid blocking.

