package tetrago.polaris.core

import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module
import tetrago.polaris.app.ui.canvas.CanvasPainter
import tetrago.polaris.core.save.SaveModule
import tetrago.polaris.core.ui.SystemView
import tetrago.polaris.core.ui.toolbar.ToolbarModule
import tetrago.polaris.core.ui.window.WindowModule
import tetrago.polaris.module.ModuleProvider
import tetrago.polaris.module.Polaris

@Polaris
class CoreModule : ModuleProvider {
    override val id = "tetrago.polaris.core"
    override val name = "Core"

    override val dependencies: List<String> = listOf()

    override val modules: List<Module>
        get() = listOf(
            ToolbarModule().module,
            WindowModule().module,
            SaveModule().module,
            module {
                single<CanvasPainter> { SystemView() }
            }
        )
}
