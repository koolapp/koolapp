package org.koolapp.web

import org.koolapp.template.*

import javax.servlet.http.*

/**
 * Represents a HTTP web request
 */
open class HttpRequestContext(uri: String, request: HttpServletRequest, response: HttpServletResponse): RequestContext(uri) {

}