package org.koolapp.website

import kotlin.io.*

import java.io.File
import org.pegdown.Extensions
import org.pegdown.PegDownProcessor
import org.pegdown.LinkRenderer
import org.koolapp.template.Template
import org.koolapp.template.renderTo

class SiteGenerator(val sourceDir: File, val outputDir: File) : Runnable {
    public var markdownProcessor: PegDownProcessor = PegDownProcessor(Extensions.ALL)
    public var linkRendered: LinkRenderer = LinkRenderer()

    public override fun run() {
        println("Generating the site to $outputDir")

        sourceDir.recurse {
            if (it.isFile()) {
                var relativePath = sourceDir.relativePath(it)
                println("Processing ${relativePath}")
                var output: String? = null
                if (it.extension == "md") {
                    val text = it.readText()
                    output = markdownProcessor.markdownToHtml(text, linkRendered) ?: ""
                    relativePath = relativePath.trimTrailing(it.extension) + "html"
                } else if (it.extension == "html") {
                    output = it.readText()
                }
                val outFile = File(outputDir, relativePath)
                outFile.directory.mkdirs()
                if (output != null) {
                    val template = layout(relativePath, it, output.sure())
                    template.renderTo(outFile)
                } else {
                    it.copyTo(outFile)
                }
            }
        }
    }

    /**
     * Applies a layout to the given file
     */
    fun layout(uri: String, file: File, text: String): Template {
        return DefaultLayoutTemplate(text)
    }
}