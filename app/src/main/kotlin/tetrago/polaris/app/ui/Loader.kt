package tetrago.polaris.app.ui

import javafx.fxml.FXMLLoader
import javafx.scene.layout.Pane
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.module.ModuleProvider

object Loader {
    fun <T> loadFxml(path: String, module: ModuleProvider? = null): Pair<Pane, T> {
        val classpath = module?.javaClass ?: MainApplication::class.java
        val loader = FXMLLoader(classpath.getResource(path))
        module?.let {
            loader.classLoader = ModuleLoader.moduleMap[it]
        }

        return loader.load<Pane>() to loader.getController()
    }
}
