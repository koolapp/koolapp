package test.koolapp.javafx

import org.koolapp.javafx.WebApplication
import test.koolapp.myapp.myApp

public class SampleWebApp(): WebApplication() {

    override fun loadInitial(): String {
        /*
        This is another way of registering an application on a page instead of using the <script> tag for the kotlin

        ready {
            myApp()
        }
        */
        return load("file://src/test/resources/sample.html")
    }
}

