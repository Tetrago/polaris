package tetrago.polaris.core

import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module
import tetrago.polaris.app.data.singleDataStore
import tetrago.polaris.app.ui.canvas.CanvasPainter
import tetrago.polaris.core.data.CoreDataStore
import tetrago.polaris.core.data.DataModule
import tetrago.polaris.core.save.SaveModule
import tetrago.polaris.core.ui.SystemView
import tetrago.polaris.core.ui.toolbar.ToolbarModule
import tetrago.polaris.core.ui.window.WindowModule
import tetrago.polaris.module.ModuleProvider
import tetrago.polaris.module.Mod

@Mod
class CoreModule : ModuleProvider {
    companion object {
        const val ID = "tetrago.polaris.core"
    }

    override val id = ID
    override val name = "Core"

    override val dependencies: List<String> = listOf()

    override val modules: List<Module>
        get() = listOf(
            DataModule().module,
            SaveModule().module,
            ToolbarModule().module,
            WindowModule().module,
            module {
                singleDataStore<CoreDataStore>()
                single<CanvasPainter> { SystemView() }
            }
        )
}
