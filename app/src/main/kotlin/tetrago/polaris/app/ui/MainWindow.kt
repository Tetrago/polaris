package tetrago.polaris.app.ui

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import tetrago.polaris.app.ui.canvas.MainCanvas
import tetrago.polaris.app.ui.controller.MainController
import tetrago.polaris.app.ui.toolbar.ToolbarProvider

class MainWindow(stage: Stage) : KoinComponent {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    val canvas: MainCanvas

    private val toolbars: List<ToolbarProvider> by lazy { getKoin().getAll() }
    private val controller: MainController

    init {
        stage.title = "Polaris"
        stage.onCloseRequest = EventHandler { Platform.exit() }
        stage.icons.add(MainApplication.icon)

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
