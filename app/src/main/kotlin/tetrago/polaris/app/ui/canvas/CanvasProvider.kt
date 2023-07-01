package tetrago.polaris.app.ui.canvas

interface CanvasProvider {
    val colors: Colors
    val scale: Double

    fun repaint()
}
