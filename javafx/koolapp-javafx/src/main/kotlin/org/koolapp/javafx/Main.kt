package org.koolapp.javafx

import javafx.application.Application

/**
 * A command line application which starts the JavaFX browser and boots up
 * the application defined by the first URL parameter.
 */
fun main(args: Array<String>): Unit {
    println("Starting KoolApp browser: http://koolapp.org/ - pleasae keep kool!")
    Application.launch(javaClass<WebApplication>(), args.makeString(" "))
}