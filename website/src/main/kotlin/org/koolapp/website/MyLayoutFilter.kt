package org.koolapp.website

import org.koolapp.template.FilterContext
import org.koolapp.template.Template
import org.koolapp.web.*

// TODO
//[WebFilter(displayName = "LayoutFilter", urlPatterns = array("*", "*.html"))]
class MyLayoutFilter(): LayoutServletFilter() {

    override fun findLayoutTemplate(context: FilterContext): Template? {
        val contentType = context.outputContentType
        println("content type: $contentType")
        if (contentType != null && contentType.startsWith("text/html")) {
            println("Matching for request: ${context.requestContext.uri}")
            return DefaultLayoutTemplate(context.source.text())
        } else {
            return null
        }
    }
}