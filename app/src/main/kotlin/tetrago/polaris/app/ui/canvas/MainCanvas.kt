package tetrago.polaris.app.ui.canvas

import javafx.beans.value.ObservableDoubleValue
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseDragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import org.koin.core.component.KoinComponent

class MainCanvas : Canvas(), CanvasProvider, KoinComponent {
    private val painters: List<CanvasPainter> by lazy { getKoin().getAll() }

    override val viewport = Viewport()

    init {
        widthProperty().addListener { observable ->
            viewport.width = (observable as ObservableDoubleValue).get()
            repaint()
        }

        heightProperty().addListener { observable ->
            viewport.height = (observable as ObservableDoubleValue).get()
            repaint()
        }

        addEventFilter(ScrollEvent.SCROLL) {
            viewport.zoom(it.x, it.y, it.deltaY)
            repaint()
        }

        var mouse: Pair<Double, Double>? = null

        addEventFilter(MouseEvent.MOUSE_PRESSED) { mouse = it.x to it.y }
        addEventFilter(MouseEvent.MOUSE_RELEASED) { mouse = null }

        addEventFilter(MouseDragEvent.MOUSE_DRAGGED) {
            mouse?.let { pair ->
                val dx = it.x - pair.first
                val dy = it.y - pair.second

                viewport.translate(dx, dy)

                mouse = it.x to it.y

                repaint()
            }
        }
    }

    override fun repaint() = graphicsContext2D.scope {
        painters.forEach { it.apply { prePaint(width, height) } }

        translate(width / 2, height / 2)
        translate(viewport.x, viewport.y)
        scale(viewport.scale, viewport.scale)

        painters.forEach {
            scope {
                it.apply { paint(width, height) }
            }
        }
    }

    override fun isResizable(): Boolean = true
}

fun GraphicsContext.scope(block: GraphicsContext.() -> Unit) {
    val stack = transform.clone()
    block()
    transform = stack
}
