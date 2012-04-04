package org.koolapp.web

import javax.servlet.*
import javax.servlet.http.*
import org.koolapp.template.*

class TextFilterServlet(val textFilter: TextFilter): HttpServlet() {

    public override fun doGet(request: HttpServletRequest?, response: HttpServletResponse?) {
        if (request != null && response != null) {
            val servletContext = request.getServletContext()
            if (servletContext != null) {
                // lets find the source
                val path = request.getServletPath()
                val input = servletContext.getResourceAsStream(path)
                val filterContext = if (input != null) {
                    filterContext(request, response, InputStreamInput(input))
                } else {
                    null
                }
                if (filterContext != null) {
                    val writer = response.getWriter().sure()
                    textFilter.filter(filterContext, writer)
                    writer.flush()
                    val contentType = filterContext.outputContentType
                    if (contentType != null) {
                        response.setContentType(contentType)
                    }
                } else {
                    log("Cannot find input for: $path")
                }
            }
        }
    }
}