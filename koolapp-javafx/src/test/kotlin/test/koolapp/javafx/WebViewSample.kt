package test.koolapp.javafx

import javafx.application.Application
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.HPos
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.web.WebView
import javafx.stage.Stage
import javafx.scene.layout.VBox
import javafx.scene.layout.Region

public class WebViewSample(): Application() {

    override public fun start(primaryStage: Stage?) {
        val view = WebView()
        val prefWidth = 800.0
        val prefHeight = 600.0
        view.setMinSize(100.0, 100.0)
        view.setPrefSize(prefWidth, prefHeight)
        view.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        val eng = view.getEngine()!!
        val firstLocation = "http://camel.apache.org/index.html"
        eng.load(firstLocation)
        val locationField = TextField(firstLocation)
        locationField.setMaxHeight(Double.MAX_VALUE)
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
        /*
        grid.setMaxWidth(Double.MAX_VALUE);
        grid.setMaxHeight(Double.MAX_VALUE);
        */
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        //grid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        GridPane.setConstraints(locationField, 0, 0, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.SOMETIMES)
        GridPane.setConstraints(goButton, 1, 0)
        GridPane.setConstraints(view, 0, 1, 2, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS)

        val buttonWidth = 40.0
        var gridConstraints = grid.getColumnConstraints()!!
        gridConstraints.add(ColumnConstraints(100.0, prefWidth - (buttonWidth + (2 * grid.getHgap())), Double.MAX_VALUE, Priority.ALWAYS, HPos.CENTER, true))
        gridConstraints.add(ColumnConstraints(buttonWidth, buttonWidth, Double.MAX_VALUE, Priority.SOMETIMES, HPos.CENTER, true))

        grid.getChildren()?.addAll(locationField, goButton, view)
        //root.getChildren()?.add(grid)


        //val root = Group(grid)
        //val scene = Scene(root)
        val scene = Scene(grid)
        val stage = primaryStage!!
        stage.setMaxHeight(Double.MAX_VALUE)
        stage.setMaxWidth(Double.MAX_VALUE)
        stage.setHeight(prefHeight + buttonWidth + (2 * grid.getVgap()))
        stage.setWidth(prefWidth)
        stage.setScene(scene)
        stage.show()
    }
}

