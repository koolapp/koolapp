package org.koolapp.camel.support

import org.apache.camel.Predicate
import org.apache.camel.Exchange

/**
 * An implementation of [[Predicate]] which takes a function
 */
//public class PredicateFunction(val fn: Exchange.() -> Boolean): Predicate {
public class PredicateFunction(val fn: (Exchange) -> Boolean): Predicate {
    public override fun matches(exchange: Exchange?): Boolean {
        return if (exchange != null) {
            fn(exchange)
        } else false
    }

    public override fun toString(): String {
        return "PredicateFunction($fn)"
    }
}