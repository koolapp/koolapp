package org.koolapp.template.markdown

import org.koolapp.template.*

import org.pegdown.PegDownProcessor
import org.pegdown.Extensions
import org.pegdown.LinkRenderer

/**
 * Converts markdown into HTML
 */
class MarkdownFilter : TextFilter {
    public var markdownProcessor: PegDownProcessor = PegDownProcessor(Extensions.ALL)
    public var linkRendered: LinkRenderer = LinkRenderer()

    fun toString() = "MarkdownFilter"

    override fun filter(filterContext: FilterContext, appendable: Appendable) {
        val text = filterContext.source.text()
        val output = markdownProcessor.markdownToHtml(text, linkRendered)
        if (output != null) {
            appendable.append(output)
        }
    }

    override val urlMapping: Array<String> = array("*.md")
}