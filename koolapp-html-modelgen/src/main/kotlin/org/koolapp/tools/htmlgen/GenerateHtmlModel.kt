package org.koolapp.tools.htmlgen

import org.koolapp.html.*
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
 * Parses the HTML5 specification
 */
class GenerateHtmlModel : Runnable {

    public var htmlSpecUrl: String = "http://dev.w3.org/html5/spec/section-index.html"
    public var htmlGlobalAttributesUrl: String = "http://dev.w3.org/html5/spec/global-attributes.html"

    public override fun run() {
        println("Loading the HTML5 spec from $htmlSpecUrl")

        val document = if (htmlSpecUrl.startsWith("file://")) {
            val file = htmlSpecUrl.substring("file://".length())
            val inputSource = InputSource(FileInputStream(file))
            parseHtml(inputSource)
        }
        else {
            parseHtml(htmlSpecUrl)
        }
        processDocument(document)
    }

    fun processDocument(document: Document): Unit {
        val title = document.id("elements-1")!!

        // lets find the next table element
        println("Found title ${title.text}")

        val nextElements: Iterator<Element> = title.nextElements()
        val table = nextElements.find{ it.getTagName() == "table" }
        println("Found table $table")
        if (table != null) {
            val tbody = table["tbody"]
            if (tbody.notEmpty()) {
                val rows = tbody[0]["tr"]
                for (row in rows) {
                    //println("Processing row $row")
                    val links = row["a"]
                    if (links.notEmpty()) {
                        val name = links[0].text
                        val tds = row["td"]
                        var empty = false
                        if (tds.size() > 4) {
                            val contentTd = tds[1]
                            val emptyTd = tds[3]
                            if (emptyTd.text == "empty") {
                                empty = true
                            }
                            val attributesTd = tds[4]
                            // TODO drop 1
                            val attributeNames = attributesTd["a"].map<Element,String>{it.text}
                            println("Element $name empty $empty attributes $attributeNames")
                        }
                    }
                }
            }

        }

    }
}

