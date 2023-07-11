package tetrago.polaris.app.koin

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

interface ModuleKoinComponent : KoinComponent {
    override fun getKoin(): Koin = ModuleKoinContext.koin
}