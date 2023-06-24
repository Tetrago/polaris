package tetrago.polaris.core.ui

import javafx.scene.canvas.GraphicsContext

fun GraphicsContext.fillCircle(x: Double, y: Double, r: Double) {
    fillOval(x - r, y - r, r * 2, r * 2)
}