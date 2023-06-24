package tetrago.polaris.sol

import org.koin.core.module.Module
import org.koin.dsl.module
import tetrago.polaris.app.save.SaveDataWriter
import tetrago.polaris.module.ModuleProvider
import tetrago.polaris.module.Polaris
import tetrago.polaris.sol.save.Writer

@Polaris
class SolModule : ModuleProvider {
    override val id: String = "tetrago.polaris.sol"
    override val name: String = "Sol System"

    override val dependencies: List<String> = listOf("tetrago.polaris.core")

    override val modules: List<Module> = listOf(module {
        single<SaveDataWriter> { Writer() }
    })
}
