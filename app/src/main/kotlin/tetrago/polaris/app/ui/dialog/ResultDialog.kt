package tetrago.polaris.app.ui.dialog

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.ui.Loader
import tetrago.polaris.app.ui.MainApplication
import tetrago.polaris.module.ModuleProvider
import kotlin.reflect.KClass

abstract class ResultDialog<T, R>(title: String, path: String, module: ModuleProvider? = null) : Stage() {
    val controller: T
    protected var result: R? = null

    init {
        this.title = title
        onCloseRequest = EventHandler { close() }

        Loader.loadFxml<T>(path, module).also {
            scene = Scene(it.first)
            controller = it.second
        }
    }

    fun prompt(): R? {
        showAndWait()
        return result
    }
}
