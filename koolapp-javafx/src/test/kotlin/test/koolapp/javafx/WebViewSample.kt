package test.koolapp.javafx

import org.koolapp.javafx.WebApplication
import test.koolapp.javafx.templates.sampleTemplate

public class WebViewSample(): WebApplication() {

    override fun loadInitial(): String {
        ready {
            val newNode = sampleTemplate(it)
            println("About to insert DOM node $newNode")
            val container = it.getElementById("view")
            if (container != null) {
                container.appendChild(newNode)
                println("Added new node!!!")
            } else println("Cannot find container!")
        }

        return load("file://src/test/resources/sample.html")
    }
}

