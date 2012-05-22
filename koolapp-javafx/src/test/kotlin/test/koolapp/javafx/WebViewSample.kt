package test.koolapp.javafx

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.concurrent.Worker.State
import org.koolapp.javafx.WebApplication
import test.koolapp.javafx.templates.sampleTemplate

public class WebViewSample(): WebApplication() {

    override fun loadInitial(): String {
        val initialLocation = "file://src/test/resources/sample.html"
        load(initialLocation)
        val changeListener = object : ChangeListener<State?> {
            public override fun changed(observable: ObservableValue<out State?>?, oldValue: State?, newValue: State?) {
                println("Loaded initial URL!")
                val document = engine.getDocument()
                if (document != null) {
                    val newNode = sampleTemplate(document)
                    println("About to insert DOM node $newNode")
                    val container = document.getElementById("view")
                    if (container != null) {
                        container.appendChild(newNode)
                        println("Added new node!!!")
                    } else println("Cannot find container!")
                }
            }
        }
        engine.getLoadWorker()?.stateProperty()?.addListener(changeListener)
        return initialLocation
    }
}

