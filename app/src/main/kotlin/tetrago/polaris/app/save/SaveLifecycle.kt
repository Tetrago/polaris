package tetrago.polaris.app.save

import org.koin.core.context.loadKoinModules
import tetrago.polaris.app.data.DataStoreProvider
import tetrago.polaris.app.koin.getModuleKoin
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.module.ModuleProvider

object SaveLifecycle {
    private inline fun <reified T : Any> ModuleProvider.all() : List<T> {
        return getModuleKoin().getAll<T>().filter { it.javaClass.classLoader == ModuleLoader.moduleMap[this] }
    }

    fun read(file: SaveFile) {
        val queue = mutableListOf<Iterator<SaveLifecycleProvider>>()

        file.modules.forEach { module ->
            loadKoinModules(module.modules)

            module.all<DataStoreProvider>().forEach { it.load(file.directory) }
            module.all<SaveDataReader>().forEach { it.read() }

            queue.add(module.all<SaveLifecycleProvider>().iterator())
        }

        while(queue.any { it.hasNext() }) {
            queue.forEach {
                it.next().read(file)
            }
        }
    }
}