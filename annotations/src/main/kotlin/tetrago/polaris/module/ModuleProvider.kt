package tetrago.polaris.module

import org.koin.core.module.Module

interface ModuleProvider {
    val modules: List<Module>
}
