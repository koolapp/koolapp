package org.koolapp.website

import javax.servlet.annotation.WebFilter
import org.koolapp.template.FilterContext
import org.koolapp.web.*
import org.koolapp.template.Output

// TODO
//[WebFilter(displayName = "LayoutFilter", urlPatterns = array("*", "*.html"))]
class MyLayout() : LayoutFilter() {

    override fun findLayout(context: FilterContext): Layout? {
        val contentType = context.outputContentType
        println("content type: $contentType")
        if (contentType != null && contentType.startsWith("text/html")) {
            println("Matching for request: ${context.requestContext.uri}")
            return LayoutTemplate(SomeLayout(context))
        } else {
            return null
        }
    }
}