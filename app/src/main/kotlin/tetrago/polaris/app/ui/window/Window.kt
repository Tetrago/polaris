package tetrago.polaris.app.ui.window

import javafx.scene.layout.Pane
import org.koin.core.component.KoinComponent

abstract class Window(val tag: String, val title: String) : Pane(), KoinComponent
