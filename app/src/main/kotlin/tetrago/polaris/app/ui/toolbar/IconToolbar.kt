package tetrago.polaris.app.ui.toolbar

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tetrago.polaris.app.ui.window.WindowService
import java.io.InputStream

open class IconToolbar(
    private val label: String,
    private val window: String,
    private val icon: InputStream
) : ToolbarProvider, KoinComponent {
    private val windowService: WindowService by inject()

    override fun build(): Button {
        return Button().apply {
            tooltip = Tooltip(label)
            graphic = ImageView(Image(icon)).apply {
                fitWidth = 32.0
                fitHeight = 32.0
            }
            onAction = EventHandler {
                windowService.open(window)
            }
        }
    }
}
