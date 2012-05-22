## Kool JavaFX

This module requires Java 7 update 4 or later which ships with JavaFX.

To run the sample try...

    mvn -Pui

Assuming you've Java 7 enabled and JAVA_HOME points to the JRE/JDK for Java 7 or later.

The demo should create a really simple Java application that boots up a browser, loads a local file then dynamically updates the DOM using [Kool Templates](http://koolapp.org/templates.html).

Here's a breakdown of the source code used to implement this:

* [test.koolapp.myapp.MyApp.kt](https://github.com/koolapp/koolapp/blob/master/koolapp-javafx/src/test/kotlin/test/koolapp/myapp/MyApp.kt) the actual application
* [test.koolapp.javafx.Main.kt](https://github.com/koolapp/koolapp/blob/master/koolapp-javafx/src/test/kotlin/test/koolapp/javafx/Main.kt) : Java main() function launcher for the JavaFX web app
* [test.koolapp.javafx.SampleWebApp.kt](https://github.com/koolapp/koolapp/blob/master/koolapp-javafx/src/test/kotlin/test/koolapp/javafx/SampleWebApp.kt) : JavaFX web app (i.e. a kinda browser app which then loads our myApp function when the browser is ready and the single page HTML)

The application code - the [myapp() function](https://github.com/koolapp/koolapp/blob/master/koolapp-javafx/src/test/kotlin/test/koolapp/myapp/MyApp.kt) should be usable when compiled to JavaScript directly. The code in the javafx package is only required if you want to run the application on a JVM with JavaFX