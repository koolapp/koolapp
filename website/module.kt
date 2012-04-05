import kotlin.modules.*

fun project() {
    module("website") {
        addSourceFiles("../koolapp-stream/src/main/kotlin")
        //addSourceFiles("../koolapp-template/src/main/kotlin")
    }
}