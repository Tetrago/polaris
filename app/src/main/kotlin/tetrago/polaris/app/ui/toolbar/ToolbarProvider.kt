package tetrago.polaris.app.ui.toolbar

import javafx.scene.control.Button

interface ToolbarProvider {
    fun build(): Button
}
