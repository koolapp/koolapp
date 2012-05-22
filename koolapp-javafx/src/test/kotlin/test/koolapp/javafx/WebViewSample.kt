package test.koolapp.javafx

import org.koolapp.javafx.WebApplication

public class WebViewSample(): WebApplication() {

    override fun loadInitial(): String {
        ready {
            myApp(it)
        }
        return load("file://src/test/resources/sample.html")
    }
}

