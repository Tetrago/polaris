package tetrago.polaris.app.ui.window

interface WindowServiceProvider {
    fun open(tag: String)
    fun close(tag: String)
}
