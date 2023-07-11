package tetrago.polaris.app.ui.canvas

import kotlin.math.sign

class Viewport {
    var fixedScale: Float = 0.0001f
    var scale: Float = 1f
    var scrollSpeed: Float = 0.1f
    var x: Float = 0f
    var y: Float = 0f

    var width: Float = 0f
    var height: Float = 0f

    fun zoom(mouseX: Float, mouseY: Float, delta: Float) {
        scale *= 1 + scrollSpeed * sign(delta)

        translate(
            (mouseX - width / 2 - x) * scrollSpeed * -sign(delta),
            (mouseY - height / 2 - y) * scrollSpeed * -sign(delta)
        )
    }

    fun translate(dx: Float, dy: Float) {
        x += dx
        y += dy
    }
}
