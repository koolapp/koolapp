package test.koolapp.template.html

import org.koolapp.template.html.*
import org.w3c.dom.*

import kotlin.dom.*

import org.junit.Test

class HtmlTemplateTest {
    Test fun createDom() {
        val document = createDocument()
        val dom = document.html {
            body {
                h1("Hey") {
                }
                h2 {
                }
                p {
                    a(href = "hey", text = "link text") {
                    }
                    a(href = "cheese.html", title = "my link title") {
                        img(src = "blah.jpg") {
                        }
                    }
                }

            }
        }
        println("Xml is ${dom.toXmlString()}")
    }
}