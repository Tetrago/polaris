package tetrago.polaris.app.ui

import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color

class MainCanvas : Canvas() {
    init {
        widthProperty().addListener { _ -> repaint() }
        heightProperty().addListener { _ -> repaint() }
    }

    fun repaint() = graphicsContext2D.apply {
        fill = Color.BLUE
        fillRect(0.0, 0.0, width, height)
    }

    override fun isResizable(): Boolean = true
}