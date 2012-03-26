package org.koolapp.web

import java.io.*
import javax.servlet.*
import javax.servlet.http.*

import org.koolapp.template.ByteArrayInput
import org.koolapp.template.FilterContext
import org.koolapp.template.Output
import org.koolapp.template.Template
import org.koolapp.web.support.*

import java.util.List

/**
 * A Servlet [[Filter]] which performs layouts
 */
abstract class LayoutServletFilter: Filter {

    public var config: FilterConfig? = null

    public var urlMapping: Array<String> = array("*")

    override fun init(config: FilterConfig?) {
        $config = config
    }

    override fun destroy() {
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        if (chain != null) {
            if (request is HttpServletRequest && response is HttpServletResponse) {
                val path = request.getServletPath()
                val sc = request.getServletContext()

                val wrapRequest: HttpServletRequest = request
                //val wrapRequest = HttpServletRequestWrapper(request)
                val wrapResponse = BufferedResponseWrapper(response);
                chain.doFilter(wrapRequest, wrapResponse)

                val bytes = wrapResponse.toBytes()
                val context = filterContext(wrapRequest, wrapResponse, ByteArrayInput(bytes))
                val status = wrapResponse.getStatus()
                println("path: $path status: $status")
                val bytesSize = bytes.size
                val hasData = bytesSize > 0
                val layout = if (context != null && hasData && status >= 200 && status < 300) {
                    context.outputContentType = wrapResponse.getContentType() ?: request.getContentType()
                    findLayoutTemplate(context)
                } else null
                if (context != null && layout != null) {
                    val writer = response.getWriter().sure()
                    layout.render(writer)
                    writer.flush()
                    response.setContentLength(bytesSize)
                } else {
                    sc?.log("No layout found for $path")
                    if (hasData) {
                        response.getOutputStream()?.write(bytes)
                    }
                }
            } else {
                chain.doFilter(request, response)
            }
        }
    }

    /**
     * Returns the [[Template]] used to layout the current source
     */
    protected open fun findLayoutTemplate(context: FilterContext): Template? {
        return null
    }
}
