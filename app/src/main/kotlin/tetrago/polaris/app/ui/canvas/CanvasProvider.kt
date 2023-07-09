package tetrago.polaris.app.ui.canvas

interface CanvasProvider {
    val viewport: Viewport

    fun repaint()
}
