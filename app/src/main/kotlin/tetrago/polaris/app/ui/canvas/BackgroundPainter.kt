package tetrago.polaris.app.ui.canvas

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.koin.core.component.KoinComponent

class BackgroundPainter : CanvasPainter(), KoinComponent {
    var backgroundColor: Color by hold(Color.BLACK)

    override fun GraphicsContext.prePaint(width: Double, height: Double) {
        fill = backgroundColor
        fillRect(0.0, 0.0, width, height)
    }
}