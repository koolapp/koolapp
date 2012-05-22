package test.koolapp.javafx.templates

import org.koolapp.template.html.*
import org.w3c.dom.Document
import org.w3c.dom.Node
import kotlin.dom.*

fun sampleTemplate(document: Node): Node {
    return document.div {
        h1("Template Generated Content!")
    }
}