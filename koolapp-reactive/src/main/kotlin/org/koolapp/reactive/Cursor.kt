package org.koolapp.reactive

import java.io.Closeable

/**
 * Represents the processing of a [[Stream]] by a [[Handler]]
 * which can be closed via the [[Closeable]] interface
 */
trait Cursor: Closeable {
    fun isClosed(): Boolean
}