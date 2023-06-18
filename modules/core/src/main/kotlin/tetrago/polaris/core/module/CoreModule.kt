package tetrago.polaris.core.module

import org.koin.core.module.Module
import org.koin.ksp.generated.module
import tetrago.polaris.core.ui.toolbar.ToolbarModule
import tetrago.polaris.core.ui.window.WindowModule
import tetrago.polaris.module.PolarisModule
import tetrago.polaris.module.ModuleProvider

@PolarisModule
class CoreModule : ModuleProvider {
    override val id = "tetrago.polaris.core"
    override val name = "Core"

    override val modules: List<Module>
        get() = listOf(
            ToolbarModule().module,
            WindowModule().module
        )
}
