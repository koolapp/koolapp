## Kool Templates

**Kool Templates** generate HTML5 markup as a DOM using a simple statically typed template language written in [Kotlin](http://jetbrains.github.com/kotlin/).

They are designed so that they can be used on the client side inside a web browser or used on the server side in a Servlet / JAXRS application.

Using Kool Templates its easy to create an entire page or DOM fragment from any function or class. However longer term we hope to allow active views to be created that automatically keep the DOM view updated as the underlying model changes  using [Kool Streams](https://github.com/koolapp/koolapp/blob/master/koolapp-stream/ReadMe.md) to monitor your domain model or external events and update the correct parts of your view automatically.

### Examples

The easiest way to get started is to [check out an example](https://github.com/koolapp/koolapp/blob/master/koolapp-template/src/test/kotlin/test/koolapp/template/html/HtmlTemplateTest.kt#L12)

