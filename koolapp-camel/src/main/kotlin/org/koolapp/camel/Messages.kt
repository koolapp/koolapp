package org.koolapp.camel

import org.apache.camel.Exchange
import org.apache.camel.Message

var Message.body: Any?
    get() = getBody()
    set(value) {
        setBody(value)
    }
