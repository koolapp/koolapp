package test.koolapp.javafx

import org.koolapp.javafx.WebApplication
import test.koolapp.myapp.myApp

public class SampleWebApp(): WebApplication() {

    override fun loadInitial(): String {
        ready {
            myApp(it)
        }
        return load("file://src/test/resources/sample.html")
    }
}

