package tetrago.polaris.app.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import com.sksamuel.hoplite.fp.invalid
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.get
import tetrago.polaris.app.koin.getModuleKoin

@OptIn(ExperimentalTextApi::class)
class MainCanvas : CanvasProvider {
    private val painters: List<CanvasPainter> by lazy { getModuleKoin().getAll() }

    override val viewport = Viewport()
    override val textMeasurer: TextMeasurer get() = _textMeasurer.value

    lateinit var _textMeasurer: MutableState<TextMeasurer>

    val invalidations = mutableStateOf(0)

    override fun repaint() {
        ++invalidations.value
    }

    @Composable
    fun paint() {
        val invalidations by remember { invalidations }

        Canvas(modifier = Modifier.fillMaxSize()) {
            viewport.width = size.width
            viewport.height = size.height

            invalidations.let {
                painters.forEach { with(it) { prePaint() } }

                withTransform({
                    translate(size.width * 0.5f, size.height * 0.5f)
                    translate(viewport.x, viewport.y)
                    scale(viewport.scale, viewport.scale)
                }) {
                    painters.forEach {
                        with(it) { paint() }
                    }
                }
            }
        }
    }
}
