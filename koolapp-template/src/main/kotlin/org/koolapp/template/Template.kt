package org.koolapp.template

/**
 * Represents a textual template
 */
abstract class Template {
    abstract fun render(out: Appendable): Unit
}