package org.koolapp.template.html

import org.w3c.dom.*

import kotlin.dom.*

import org.koolapp.template.Template
import java.io.Writer

/**
 * Renders the node as XML markup
 */
fun Node.render(out: Appendable): Unit {
    if (out is Writer) {
        writeXmlString(out, false)
    } else {
        out.append(toXmlString())
    }
}

fun Node.element(localName: String, init: Element.()-> Unit): Element {
    val element = ownerDocument().createElement(localName).sure()
    element.init()
    if (this is Element) {
        appendChild(element)
    }
    return element
}

fun Node.textElement(localName: String, text: String? = null, init: Element.()-> Unit): Element {
    return element(localName) {
        if (text != null) addText(text)
        init()
    }
}

fun Node.html(init: Element.()-> Unit): Element = element("html", init)

fun Node.body(init: Element.()-> Unit): Element = element("body", init)

fun Node.div(init: Element.()-> Unit): Element = element("div", init)

fun Node.h1(text: String? = null): Element = h1(text) {}

fun Node.h1(text: String? = null, init: Element.()-> Unit): Element = textElement("h1", text, init)

fun Node.h2(text: String? = null, init: Element.()-> Unit): Element = textElement("h2", text, init)

fun Node.h3(text: String? = null, init: Element.()-> Unit): Element = textElement("h3", text, init)

fun Node.h4(text: String? = null, init: Element.()-> Unit): Element = textElement("h4", text, init)

fun Node.p(init: Element.()-> Unit): Element = element("p", init)

fun Node.a(href: String? = null, title: String? = null, text: String? = null, init: Element.()-> Unit): Element {
    return textElement("a", text) {
        if (href != null) setAttribute("href", href)
        if (title != null) setAttribute("title", title)
        init()
    }
}


fun Node.img(src: String? = null, alt: String? = null): Element = img(src, alt) {}

fun Node.img(src: String? = null, alt: String? = null, init: Element.()-> Unit): Element {
    return element("img") {
        if (src != null) setAttribute("src", src)
        if (alt != null) setAttribute("alt", alt)
        init()
    }
}


