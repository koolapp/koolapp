package org.koolapp.web

import java.io.*
import javax.servlet.*
import javax.servlet.http.*

import org.koolapp.template.ByteArrayInput
import org.koolapp.template.FilterContext
import org.koolapp.template.Output
import org.koolapp.template.Template
import org.koolapp.web.filterContext

import java.util.List
import kotlin.util.arrayList

/**
 * A Servlet [[Filter]] which performs layouts
 */
abstract class LayoutFilter: Filter {

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
                val layout = if (context != null && bytes.size > 0 && status >= 200 && status < 300) {
                    context.outputContentType = wrapResponse.getContentType() ?: request.getContentType()
                    findLayout(context)
                } else null
                if (context != null && layout != null) {
                    layout.layout(context, ResponseOutput(response))
                } else {
                    sc?.log("No layout found for $path")
                    response.getOutputStream()?.write(bytes)
                }
            } else {
                chain.doFilter(request, response)
            }
        }
    }

    protected open fun findLayout(context: FilterContext): Layout? {
        return null
    }
}

trait Layout {
    fun layout(context: FilterContext, output: Output)
}

class LayoutTemplate(val template: Template): Layout {

    override fun layout(context: FilterContext, output: Output) {
        val writer = output.writer()
        template.render(writer)
    }
}
class ResponseOutput(val response: ServletResponse): Output() {

    override fun outputStream(): OutputStream {
        return response.getOutputStream().sure()
    }

    override fun writer(): Writer {
        return response.getWriter().sure()
    }
}

class BufferedResponseWrapper(response: HttpServletResponse): HttpServletResponseWrapper(response) {
    val buffer = ByteArrayOutputStream();

    fun toBytes(): ByteArray {
        return buffer.toByteArray().sure()
    }

    override fun getWriter(): PrintWriter {
        return PrintWriter(OutputStreamWriter(buffer));
    }

    override fun getOutputStream(): ServletOutputStream? {
        return BufferedServletOutputStream(buffer)
    }
}

class BufferedServletOutputStream(val buffer: ByteArrayOutputStream) : ServletOutputStream() {

    override fun write(b: Int) {
        buffer.write(b)
    }
}

class TextBufferResponseWrapper(response: HttpServletResponse): HttpServletResponseWrapper(response) {
    val output = StringWriter();

    fun getOutputText(): String {
        return output.toString().sure()
    }

    override fun getWriter(): PrintWriter {
        return PrintWriter(output);
    }
}
