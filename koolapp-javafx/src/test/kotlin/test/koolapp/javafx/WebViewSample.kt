package test.koolapp.javafx

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.geometry.VPos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView


public class WebViewSample() : Application() {
 
    private fun init(primaryStage: Stage) {
        val root = Group()
        primaryStage.setScene(Scene(root))
        val view = WebView()
        val minWidth = 800.0
        val minHeight = 600.0
        view.setMinSize(minWidth, minHeight)
        view.setPrefSize(minWidth, minHeight)
        val eng = view.getEngine()!!
        val firstLocation = "http://camel.apache.org/index.html"
        eng.load(firstLocation)
        val locationField = TextField(firstLocation)
        locationField.setMaxWidth(Double.MAX_VALUE)
        val goButton = Button("Go")
        goButton.setDefaultButton(true)
        val goAction = object : EventHandler<ActionEvent?> {
            public override fun handle(event: ActionEvent?) {
                val location = locationField.getText() ?: ""
                if (location.notEmpty()) {
                    val fullUrl = if (location.startsWith("http://")) location else
                        "http://" + location
                    eng.load(fullUrl)
                }
            }
        }
        goButton.setOnAction(goAction)
        locationField.setOnAction(goAction)
        val changeListener = object : ChangeListener<String?> {
            public override fun changed(observable: ObservableValue<out String?>?, oldValue: String?, newValue: String?) {
                locationField.setText(newValue)
            }
        }
        eng.locationProperty()?.addListener(changeListener)
        val grid = GridPane()
        grid.setVgap(5.0)
        grid.setHgap(5.0)
        GridPane.setConstraints(locationField, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.SOMETIMES)
        GridPane.setConstraints(goButton,1,0)
        GridPane.setConstraints(view, 0, 1, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS)

        grid.getColumnConstraints()?.addAll(
                arrayList(ColumnConstraints(100.0, 100.0, Double.MAX_VALUE, Priority.ALWAYS, HPos.CENTER, true),
                ColumnConstraints(40.0, 40.0, 40.0, Priority.NEVER, HPos.CENTER, true))
        )
        grid.getChildren()?.addAll(locationField, goButton, view)
        root.getChildren()?.add(grid)
    }
 
    override public fun start(primaryStage: Stage?) {
        init(primaryStage!!)
        primaryStage?.show()
    }
}

