package tetrago.polaris.app.ui.canvas

interface CanvasProvider {
    val colors: Colors

    fun repaint()
}
