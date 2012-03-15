package org.koolapp.template

import java.io.Reader
import java.io.File
import java.io.InputStream
import java.io.FileInputStream
import java.io.FileReader
import java.net.URL


/**
 * Represents a source file
 */
abstract class Source() {

    //var encoding = defaultEncoding

    /**
     * Returns the text of the source
     */
    abstract fun text(): String

    /**
     * Returns the [[Reader]] of the source
     */
    abstract fun reader(): Reader

    /**
     * Returns the [[InputStream]] of the source
     */
    abstract fun inputStream(): InputStream
}

class FileSource(val file: File): Source() {

    //var encoding = defaultEncoding

    override fun inputStream(): InputStream = FileInputStream(file)

    override fun reader(): Reader = FileReader(file)

    override fun text(): String = file.readText() // TODO use encoding?
}

class URLSource(val url: URL): Source() {

    override fun inputStream(): InputStream = url.openStream().sure()

    override fun reader(): Reader = inputStream().reader

    override fun text(): String = url.readText()  // TODO use encoding?
}


class InputStreamSource(val inputStream: InputStream): Source() {

    override fun inputStream(): InputStream = inputStream

    override fun reader(): Reader = inputStream().reader // TODO use encoding?

    override fun text(): String = reader().readText()
}

