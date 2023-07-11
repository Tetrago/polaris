package tetrago.polaris.app.ui.canvas

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer

interface CanvasProvider {
    val viewport: Viewport
    @OptIn(ExperimentalTextApi::class)
    val textMeasurer: TextMeasurer

    fun repaint()
}
