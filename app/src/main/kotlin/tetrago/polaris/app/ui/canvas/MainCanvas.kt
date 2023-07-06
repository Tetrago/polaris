package tetrago.polaris.app.ui.canvas

import javafx.scene.canvas.Canvas
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainCanvas : Canvas(), CanvasProvider, KoinComponent {
    private val painters: List<CanvasPainter> by lazy { getKoin().getAll() }

    override val colors = Colors()
    override val scale = 0.0001

    init {
        widthProperty().addListener { _ -> repaint() }
        heightProperty().addListener { _ -> repaint() }
    }

    override fun repaint() = graphicsContext2D.run {
        fill = colors.background
        fillRect(0.0, 0.0, width, height)

        painters.forEach { it.paint(width, height, this) }
    }

    override fun isResizable(): Boolean = true
}
