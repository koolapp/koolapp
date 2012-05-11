package org.koolapp.camel

import org.apache.camel.Exchange

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


/**
 * Returns the input message body as a String using the empty string if its null
 */
inline fun Exchange.bodyString(nullValue: String = ""): String = body<String>(javaClass<String>) ?: nullValue