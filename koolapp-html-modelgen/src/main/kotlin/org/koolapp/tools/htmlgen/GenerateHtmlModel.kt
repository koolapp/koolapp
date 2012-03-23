package org.koolapp.tools.htmlgen

import kotlin.*
import kotlin.dom.*
import kotlin.util.*
import org.w3c.dom.*

import org.cyberneko.html.HTMLConfiguration
import org.apache.xerces.parsers.DOMParser
import org.xml.sax.InputSource
import java.net.URL
import java.io.FileInputStream
import java.util.*

fun main(args: Array<String>): Unit {
    val tool = GenerateHtmlModel()
    if (args.size > 0) {
        tool.htmlSpecUrl = args[0]
    }
    tool.run()
}

/**
 * Returns the element with the given id or throws an exception if it can't be found
 */
fun Document.id(idValue: String): Element {
    val element = getElementById(idValue)
    if (element != null) {
        return element
    } else {
        // OK lets try iterating through the elements instead
        for (e in elements) {
            val value = e.attribute("id")
            if (value == idValue) {
                return e
            }
        }
        throw IllegalArgumentException("No element with id $idValue is present in document $this")
    }
}

/**
 * Parses the HTML5 specification
 */
class GenerateHtmlModel : Runnable {

    public var htmlSpecUrl: String = "http://dev.w3.org/html5/spec/section-index.html"

    override fun run() {
        println("Loading the HTML5 spec from $htmlSpecUrl")

        val config = HTMLConfiguration()
        config.setProperty("http://cyberneko.org/html/properties/names/elems", "lower")
        val parser = DOMParser(config)
        if (htmlSpecUrl.startsWith("file://")) {
            val file = htmlSpecUrl.substring("file://".length())
            val inputSource = InputSource(FileInputStream(file))
            parser.parse(inputSource)
        }
        else {
            parser.parse(htmlSpecUrl)
        }
        val document = parser.getDocument()
        if (document != null) {
            processDocument(document)
        }
    }

    fun processDocument(document: Document): Unit {
        val title = document.id("elements-1")

        // lets find the next table element
        println("Found title ${title.text}")

        val nextElements: Iterator<Element> = title.nextElements()
        val table = nextElements.find{ it.getTagName() == "table" }
        println("Found table $table")
    }
}

