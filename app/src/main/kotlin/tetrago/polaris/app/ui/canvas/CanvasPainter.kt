package tetrago.polaris.app.ui.canvas

import javafx.scene.canvas.GraphicsContext

interface CanvasPainter {
    fun paint(width: Double, height: Double, context: GraphicsContext)
}