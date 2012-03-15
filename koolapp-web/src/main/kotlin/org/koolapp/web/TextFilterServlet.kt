package org.koolapp.web

import org.koolapp.template. *

/**
 * A Servlet for serving up output using the
 */
import javax.servlet.Servlet
import javax.servlet.ServletConfig
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TextFilterServlet(val textFilter: TextFilter): HttpServlet() {

    override fun doGet(request: HttpServletRequest?, response: HttpServletResponse?) {
        if (request != null && response != null) {
            val servletContext = request.getServletContext()
            if (servletContext != null) {
                // lets find the source
                val path = request.getServletPath()
                val input = servletContext.getResourceAsStream(path)
                if (path != null && input != null) {
                    println("Found input!!")
                    val source = InputStreamSource(input)
                    val requestContext = HttpRequestContext(path, request, response)
                    val filterContext = FilterContext(requestContext, source)
                    textFilter.filter(filterContext, response.getWriter().sure())
                } else {
                    println("Cannot find input for: $path")
                }
            }
        }
    }
}