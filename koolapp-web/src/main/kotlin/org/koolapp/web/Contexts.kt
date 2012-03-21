package org.koolapp.web

import javax.servlet.*
import javax.servlet.http.*
import org.koolapp.template.*

fun filterContext(request: HttpServletRequest, response: HttpServletResponse, source: Input): FilterContext? {
    val path = request.getServletPath()
    if (path != null) {
        val requestContext = HttpRequestContext(path, request, response)
        return FilterContext(requestContext, source)
    }
    return null
}
