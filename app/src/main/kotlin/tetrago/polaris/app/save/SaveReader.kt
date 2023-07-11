package tetrago.polaris.app.save

import org.koin.core.context.GlobalContext.get
import tetrago.polaris.app.module.ModuleLifecycleProvider

class SaveReader(private val file: SaveFile) {
    fun read() {
        SaveLifecycle.read(file)
    }

    fun init() {
        get().getAll<ModuleLifecycleProvider>().forEach { it.init() }
    }
}
