package org.koolapp.camel

import org.apache.camel.Exchange
import org.apache.camel.Message

/**
* Returns the in message body
*/
inline fun Exchange.body(): Any? {
    val message = this.getIn()
    return message?.getBody()
}

// TODO if http://youtrack.jetbrains.com/issue/KT-1751 is resolved we can omit the
// verbose klass parameter

/**
 * Returns the in message body of the given type
 */
inline fun <T> Exchange.body(klass: Class<T>): T? {
    val message = this.getIn()
    return message?.getBody<T>(klass)
}

// TODO in is a reserved word in kotlinso we can't use it as the property
var Exchange.input: Message
    get() = getIn()!!
    set(value) {
        setIn(value)
    }
var Exchange.out: Message
    get() = getOut()!!
    set(value) {
        setOut(value)
    }

/**
 * Returns the input message body as a String using the empty string if its null
 */
inline fun Exchange.bodyString(nullValue: String = ""): String = body<String>(javaClass<String>) ?: nullValue

/**
 * Provides array style access to properties on the exchange
 */
inline fun Exchange.get(propertyName: String): Any? = getProperty(propertyName)

/**
 * Provides array style access to properties on the exchange
 */
inline fun Exchange.set(propertyName: String, value: Any?): Unit = setProperty(propertyName, value)

