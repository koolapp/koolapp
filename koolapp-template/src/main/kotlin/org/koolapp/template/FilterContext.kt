package org.koolapp.template

/**
 * Represents the context of a filter request
 */
class FilterContext(val requestContext: RequestContext, val source: Source) {

    /**
     * Returns the MIME content type output
     */
    public var outputContentType: String? = null
}