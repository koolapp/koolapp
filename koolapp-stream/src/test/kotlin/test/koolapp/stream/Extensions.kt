package test.koolapp.stream

import org.koolapp.stream.Cursor

import kotlin.test.assertTrue

fun Cursor.assertClosed(): Unit {
    assertTrue(this.isClosed(), "This cursor should be closed! $this")
}