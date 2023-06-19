package tetrago.polaris.module

import org.koin.core.module.Module

interface ModuleProvider {
    val id: String
    val name: String
    val dependencies: List<String>
    val modules: List<Module>
}
