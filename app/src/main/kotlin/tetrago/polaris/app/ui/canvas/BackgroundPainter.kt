package tetrago.polaris.app.ui.canvas

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import tetrago.polaris.app.koin.ModuleKoinComponent

class BackgroundPainter : CanvasPainter(), ModuleKoinComponent {
    var backgroundColor: Color by hold(Color.Black)

    override fun DrawScope.prePaint() {
        drawRect(backgroundColor)
    }
}