package tetrago.polaris.core.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import org.koin.core.component.inject
import tetrago.polaris.app.koin.ModuleKoinComponent
import tetrago.polaris.app.ui.canvas.CanvasPainter
import tetrago.polaris.app.ui.canvas.CanvasProvider
import tetrago.polaris.core.data.Body
import tetrago.polaris.core.data.BodyTypeRegistry
import tetrago.polaris.core.data.PlanetarySystem
import tetrago.polaris.core.data.model.Orbit
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class SystemView : CanvasPainter(), ModuleKoinComponent {
    private val bodyTypeRegistry: BodyTypeRegistry by inject()
    private val canvasProvider: CanvasProvider by inject()

    var bodyColor: Color by hold(Color.Blue)
    var orbitColor: Color by hold(Color.White)
    var guideColor: Color by hold(Color.Gray)
    var guideScale by hold(0.01)
    var labelColor: Color by hold(Color.White)
    var system by hold<PlanetarySystem?>(null)

    override fun DrawScope.paint() {
        system?.bodies?.filter { it.parent.target == null }?.forEach {
            drawBody(it)
        }
    }

    @OptIn(ExperimentalTextApi::class)
    private fun DrawScope.drawBody(body: Body) {
        if(body.type.target != bodyTypeRegistry.anchor) {
            withTransform(drawOrbit(body)) {
                val radius = (body.radius.toDouble() * canvasProvider.viewport.fixedScale).toFloat()
                drawOval(bodyColor, topLeft = Offset(-radius, -radius), size = Size(radius * 2, radius * 2))

                val guideDiameter = (min(canvasProvider.viewport.width, canvasProvider.viewport.height) *
                        (1 / canvasProvider.viewport.scale) * guideScale).toFloat()
                if(guideDiameter < radius * 2) {
                    val measure = canvasProvider.textMeasurer.measure(
                        body.name!!,
                        style = TextStyle(color = labelColor, fontSize = 12.sp)
                    )

                    drawText(measure, topLeft = Offset(
                        measure.size.width * 0.5f,
                        measure.size.height * 0.5f
                    ))
                } else {
                    drawOval(
                        guideColor,
                        topLeft = Offset(-guideDiameter * 0.5f, -guideDiameter * 0.5f),
                        size = Size(guideDiameter, guideDiameter),
                        style = Stroke()
                    )
                }
            }
        }

        body.moons.forEach {
            drawBody(it)
        }
    }

    private fun DrawScope.drawOrbit(body: Body): (DrawTransform) -> Unit {
        body.orbit?.let { orbit ->
            val d =
                ((orbit.apoapsis.toDouble() - orbit.periapsis.toDouble()) * canvasProvider.viewport.fixedScale * 0.5).toFloat()
            val m =
                ((orbit.apoapsis.toDouble() + orbit.periapsis.toDouble()) * canvasProvider.viewport.fixedScale * 0.5).toFloat()
            val n = m * sqrt(1 - orbit.eccentricity.pow(2)).toFloat()

            val (dx, dy) = d * cos(orbit.skew.radians + PI).toFloat() to
                    d * sin(orbit.skew.radians + PI).toFloat()

            withTransform({
                translate(dx, dy)
                rotate(-orbit.skew.degrees.toFloat())
            }) {
                drawOval(
                    orbitColor,
                    topLeft = Offset(-m, -n),
                    size = Size(m * 2, n * 2),
                    style = Stroke(1 / canvasProvider.viewport.scale.toFloat())
                )

                return {
                    it.translate(dx, dy)
                    it.rotate(-orbit.skew.degrees.toFloat())

                    it.translate(
                        m * cos(orbit.offset.radians).toFloat(),
                        n * sin(orbit.offset.radians).toFloat()
                    )
                }
            }
        }

        return { }
    }
}
