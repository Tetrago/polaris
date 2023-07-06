package tetrago.polaris.app.save

import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.get
import tetrago.polaris.app.config.Configuration
import tetrago.polaris.app.data.DataStoreProvider
import tetrago.polaris.app.module.ModuleLoader

class SaveReader(private val save: SaveFile) {
    fun loadModules(app: KoinApplication) = app.run {
        modules(
            ModuleLoader.modules
            .filter { save.enabledModuleIds.contains(it.id) }
            .map { it.modules }
            .flatten())
    }

    fun loadDatabase() {
        get().getAll<DataStoreProvider>().forEach {
            it.load(save.directory)
        }
    }

    fun loadSaveData() {
        get().getAll<SaveDataLoader>().forEach { it.load() }
    }
}
