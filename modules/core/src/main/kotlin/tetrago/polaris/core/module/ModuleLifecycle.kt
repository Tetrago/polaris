package tetrago.polaris.core.module

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tetrago.polaris.app.module.ModuleLifecycleProvider
import tetrago.polaris.core.data.PlanetarySystemRegistry
import tetrago.polaris.core.data.PlanetarySystem_
import tetrago.polaris.core.ui.SystemView

class ModuleLifecycle : ModuleLifecycleProvider, KoinComponent {
    private val planetarySystemRegistry: PlanetarySystemRegistry by inject()
    private val systemView: SystemView by inject()

    override fun init() {
        systemView.system = planetarySystemRegistry.store
            .query(PlanetarySystem_.name.equal("Sol")).build().use { it.findFirst() }
    }
}
