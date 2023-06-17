package tetrago.polaris.app.ui

import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import tetrago.polaris.app.ui.toolbar.Toolbar
import tetrago.polaris.app.ui.window.WindowServiceProvider
import kotlin.concurrent.thread
import kotlin.math.log

class MainApplication : Application(), KoinComponent {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val windowService: WindowServiceProvider by inject()
    private val toolbars: List<Toolbar> = getKoin().getAll()

    override fun start(primaryStage: Stage?) {
        val pane = FXMLLoader.load<Pane>(MainApplication::class.java.getResource("main.fxml"))

        val toolBar = pane.children.find { it.id == "toolbar" } as ToolBar
        setupToolbar(toolBar)

        val canvasPane = pane.children.find { it.id == "canvasPane" } as Pane
        canvasPane.children.add(MainCanvas().apply {
            widthProperty().bind(canvasPane.widthProperty())
            heightProperty().bind(canvasPane.heightProperty())
        })

        primaryStage!!.apply {
            title = "Polaris"
            scene = Scene(pane, 1280.0, 720.0)
            onCloseRequest = EventHandler { Platform.exit() }
            show()
        }
    }

    private fun setupToolbar(toolBar: ToolBar) {
        logger.debug("Found {} toolbar items", toolbars.size)

        toolbars.forEach { item ->
            val icon = item.javaClass.getResourceAsStream(item.icon)
            if(icon == null) {
                logger.error("Could not load toolbar icon for `{}`", item.label)
            } else {
                toolBar.items.add(Button().apply {
                    tooltip = Tooltip(item.label)
                    graphic = ImageView(Image(item.javaClass.getResourceAsStream(item.icon))).apply {
                        fitWidth = 32.0
                        fitHeight = 32.0
                    }
                    onAction = EventHandler {
                        windowService.open(item.window)
                    }
                })
            }
        }
    }
}