package tetrago.polaris.app.ui.canvas

import javafx.scene.canvas.Canvas

class MainCanvas : Canvas(), CanvasProvider {
    override val colors = Colors()

    init {
        widthProperty().addListener { _ -> repaint() }
        heightProperty().addListener { _ -> repaint() }
    }

    override fun repaint() = graphicsContext2D.run {
        fill = colors.background
        fillRect(0.0, 0.0, width, height)
    }

    override fun isResizable(): Boolean = true
}
