package tetrago.polaris.core.ui

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.text.Font
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tetrago.polaris.app.ui.canvas.CanvasPainter
import tetrago.polaris.app.ui.canvas.CanvasProvider
import tetrago.polaris.app.ui.canvas.scope
import tetrago.polaris.core.data.Body
import tetrago.polaris.core.data.PlanetarySystem
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class SystemView : CanvasPainter(), KoinComponent {
    private val canvasProvider: CanvasProvider by inject()

    var bodyColor: Color by hold(Color.BLUE)
    var orbitColor: Color by hold(Color.WHITE)
    var guideColor: Color by hold(Color.GRAY)
    var guideScale by hold(0.01)
    var labelColor: Color by hold(Color.WHITE)
    var system by hold<PlanetarySystem?>(null)

    override fun GraphicsContext.paint(width: Double, height: Double) {
        system?.bodies?.filter { it.parent.target == null }?.forEach {
            scope {
                drawBody(it)
            }
        }
    }

    private fun GraphicsContext.drawBody(body: Body) {
        val orbit = body.orbit!!

        val d = (orbit.apoapsis.toDouble() - orbit.periapsis.toDouble()) * canvasProvider.viewport.fixedScale / 2
        val m = (orbit.apoapsis.toDouble() + orbit.periapsis.toDouble()) * canvasProvider.viewport.fixedScale / 2
        val n = m * sqrt(1 - orbit.eccentricity.pow(2))

        val (dx, dy) = d * cos(orbit.skew.radians + PI) to d * sin(orbit.skew.radians + PI)

        translate(dx, dy)
        rotate(-orbit.skew.degrees)

        stroke = orbitColor
        lineWidth = 1 / canvasProvider.viewport.scale
        strokeOval(-m, -n, m * 2, n * 2)

        translate(m * cos(orbit.offset.radians), n * sin(orbit.offset.radians))

        val radius = body.radius.toDouble() * canvasProvider.viewport.fixedScale
        fill = bodyColor
        fillOval(-radius, -radius, radius * 2, radius * 2)

        val guideDiameter = min(canvasProvider.viewport.width, canvasProvider.viewport.height) *
                (1 / canvasProvider.viewport.scale) * guideScale
        if(guideDiameter < radius * 2) {
            fill = labelColor
            font = Font.font(12 / canvasProvider.viewport.scale)
            fillText(body.name, guideDiameter, font.size / 2)
        } else {
            stroke = guideColor
            strokeOval(-guideDiameter / 2, -guideDiameter / 2, guideDiameter, guideDiameter)
        }

        body.moons.forEach {
            scope {
                drawBody(it)
            }
        }
    }
}
