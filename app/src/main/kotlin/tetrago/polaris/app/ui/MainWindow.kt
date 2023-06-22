package tetrago.polaris.app.ui

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import tetrago.polaris.app.ui.canvas.MainCanvas
import tetrago.polaris.app.ui.controller.MainController
import tetrago.polaris.app.ui.toolbar.ToolbarProvider
import tetrago.polaris.app.ui.window.WindowServiceProvider

class MainWindow(stage: Stage) : KoinComponent {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    val canvas: MainCanvas

    private val toolbars: List<ToolbarProvider> = getKoin().getAll()
    private val controller: MainController

    init {
        stage.title = "Polaris"
        stage.onCloseRequest = EventHandler { Platform.exit() }

        Loader.loadFxml<MainController>("main.fxml").also {
            stage.scene = Scene(it.first, 1280.0, 720.0)
            controller = it.second
        }

        logger.debug("Found {} toolbar items", toolbars.size)
        controller.toolbar.items.addAll(toolbars.map { it.build() })

        canvas = MainCanvas().apply {
            widthProperty().bind(controller.canvasPane.widthProperty())
            heightProperty().bind(controller.canvasPane.heightProperty())

            controller.canvasPane.children.add(this)
        }
    }
}