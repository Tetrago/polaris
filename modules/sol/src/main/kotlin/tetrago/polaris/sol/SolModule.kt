package tetrago.polaris.sol

import org.koin.core.module.Module
import org.koin.dsl.module
import tetrago.polaris.app.save.SaveDataWriter
import tetrago.polaris.module.ModuleProvider
import tetrago.polaris.module.Mod
import tetrago.polaris.sol.save.Writer

@Mod
class SolModule : ModuleProvider {
    companion object {
        const val ID = "tetrago.polaris.sol"
    }

    override val id: String = ID
    override val name: String = "Sol System"

    override val dependencies: List<String> = listOf("tetrago.polaris.core")

    override val modules: List<Module> = listOf(module {
        single<SaveDataWriter> { Writer() }
    })
}
