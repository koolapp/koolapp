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
    val answer = element(localName) {
        if (text != null) addText(text)
    }
    answer.init()
    return answer
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

fun Node.a(href: String? = null, title: String? = null, text: String? = null): Element = a(href, title, text) {}

fun Node.a(href: String? = null, title: String? = null, text: String? = null, init: Element.()-> Unit): Element {
    val answer = textElement("a", text, init)
    if (href != null) answer.setAttribute("href", href)
    if (title != null) answer.setAttribute("title", title)
    return answer
}


fun Node.img(src: String? = null, alt: String? = null): Element = img(src, alt) {}

fun Node.img(src: String? = null, alt: String? = null, init: Element.()-> Unit): Element {
    val answer = element("img") {
        if (src != null) setAttribute("src", src)
        if (alt != null) setAttribute("alt", alt)
    }
    answer.init()
    return answer
}


