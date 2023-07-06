package tetrago.polaris.core.save

import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tetrago.polaris.app.save.SaveDataLoader
import tetrago.polaris.core.data.PlanetarySystem
import tetrago.polaris.core.data.PlanetarySystemRegistry
import tetrago.polaris.core.ui.SystemView

@Single
class Loader : SaveDataLoader, KoinComponent {
    private val planetarySystemRegistry: PlanetarySystemRegistry by inject()
    private val systemView: SystemView by inject()

    override fun load() {
        systemView.system = planetarySystemRegistry.store.get(0)
    }
}
