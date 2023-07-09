package tetrago.polaris.app.ui.canvas

import kotlin.math.sign

class Viewport {
    var fixedScale: Double = 0.0001
    var scale: Double = 1.0
    var scrollSpeed: Double = 0.1
    var x: Double = 0.0
    var y: Double = 0.0

    var width: Double = 0.0
    var height: Double = 0.0

    fun zoom(mouseX: Double, mouseY: Double, delta: Double) {
        scale *= 1 + scrollSpeed * sign(delta)

        translate(
            (mouseX - width / 2 - x) * scrollSpeed * -sign(delta),
            (mouseY - height / 2 - y) * scrollSpeed * -sign(delta)
        )
    }

    fun translate(dx: Double, dy: Double) {
        x += dx
        y += dy
    }
}
