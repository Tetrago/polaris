package tetrago.polaris.core

import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module
import tetrago.polaris.app.save.SaveDataProvider
import tetrago.polaris.core.save.SaveData
import tetrago.polaris.core.ui.toolbar.ToolbarModule
import tetrago.polaris.core.ui.window.WindowModule
import tetrago.polaris.module.PolarisModule
import tetrago.polaris.module.ModuleProvider

@PolarisModule
class CoreModule : ModuleProvider {
    override val id = "tetrago.polaris.core"
    override val name = "Core"

    override val dependencies: List<String> = listOf()

    override val modules: List<Module>
        get() = listOf(
            ToolbarModule().module,
            WindowModule().module,
            module {
                single<SaveDataProvider> { SaveData() }
            }
        )
}
