package tetrago.polaris.core.ui

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tetrago.polaris.app.ui.canvas.CanvasPainter
import tetrago.polaris.app.ui.canvas.CanvasProvider
import tetrago.polaris.core.model.System

class SystemView : CanvasPainter, KoinComponent {
    private val canvasProvider: CanvasProvider by inject()

    var system: System? = null
        set(value) {
            field = value
            canvasProvider.repaint()
        }

    override fun paint(width: Double, height: Double, context: GraphicsContext) = context.run {
        val body = system?.bodies?.single { it.parent == null } ?: return

        fill = Color.BLUE
        fillCircle(width / 2, height / 2, body.radius.kilometers * canvasProvider.scale)
    }
}