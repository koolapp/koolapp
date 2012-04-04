package test.koolapp.reactive

import org.koolapp.reactive.Cursor
import kotlin.test.assertTrue

fun Cursor.assertClosed(): Unit {
    assertTrue(this.isClosed(), "This cursor should be closed! $this")
}