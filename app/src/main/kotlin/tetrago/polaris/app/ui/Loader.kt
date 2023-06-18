package tetrago.polaris.app.ui

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane

object Loader {
    fun <T> loadFxml(path: String): Pair<Pane, T> {
        return FXMLLoader(javaClass.getResource(path)).run {
            load<Pane>() to getController<T>()
        }
    }
}