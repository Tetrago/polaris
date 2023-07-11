package tetrago.polaris.app.ui.toolbar

import org.koin.core.component.inject
import org.pushingpixels.aurora.component.model.Command
import org.pushingpixels.aurora.component.projection.CommandButtonProjection
import tetrago.polaris.app.koin.ModuleKoinComponent
import tetrago.polaris.app.ui.window.Window
import tetrago.polaris.app.ui.window.WindowService
import kotlin.reflect.KClass

open class IconToolbar<in T : Window>(
    private val label: String,
    private val window: KClass<T>
) : ToolbarProvider, ModuleKoinComponent {
    private val windowService: WindowService by inject()

    override fun build() = CommandButtonProjection(
        contentModel = Command(label, action = { windowService.open(window) }),
    )
}
