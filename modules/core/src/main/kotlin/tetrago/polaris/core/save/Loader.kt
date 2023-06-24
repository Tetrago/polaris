package tetrago.polaris.core.save

import org.koin.core.annotation.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tetrago.polaris.app.save.SaveDataLoader
import tetrago.polaris.core.model.System
import tetrago.polaris.core.ui.SystemView

@Single
class Loader : SaveDataLoader, KoinComponent {
    private val systemView: SystemView by inject()

    override fun load() {
        systemView.system = System.all().first()
    }
}