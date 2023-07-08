package tetrago.polaris.app.save

import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.Pane
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import tetrago.polaris.app.data.DataStoreProvider
import tetrago.polaris.app.data.RegistryProvider
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.module.ModuleProvider
import kotlin.random.Random

class SaveLifecycle : SaveLifecycleProvider, KoinComponent {
    protected inline fun <reified T : Any> all(module: ModuleProvider): List<T> {
        return getKoin().getAll<T>().filter { it.javaClass.classLoader == ModuleLoader.moduleMap[module] }
    }

    override fun read(file: SaveFile) {
        val queue = mutableListOf<Iterator<SaveLifecycleProvider>>()

        file.modules.forEach { module ->
            loadKoinModules(module.modules)

            all<DataStoreProvider>(module).forEach { it.load(file.directory) }
            all<SaveDataReader>(module).forEach { it.read() }

            queue.add(all<SaveLifecycleProvider>(module).iterator())
        }

        while(queue.any { it.hasNext() }) {
            queue.forEach {
                it.next().read(file)
            }
        }
    }

    override fun write(file: SaveFile, random: () -> Random) {
        val queue = mutableListOf<Iterator<SaveLifecycleProvider>>()

        ModuleLoader.orderModules(file.modules).forEach { module ->
            loadKoinModules(module.modules)

            all<DataStoreProvider>(module).forEach { it.load(file.directory) }
            all<RegistryProvider>(module).forEach { it.create() }

            all<SaveDataWriter>(module).forEach { writer ->
                writer.loadConfig()?.let { loadWriterDialog(it) }
                writer.initialize(random())
            }

            queue.add(all<SaveLifecycleProvider>(module).iterator())
        }

        while(queue.any { it.hasNext() }) {
            queue.forEach {
                it.next().write(file, random)
            }
        }
    }

    private fun loadWriterDialog(pane: Pane) {
        val result = Dialog<ButtonType>().apply {
            title = "New Save"
            dialogPane.apply {
                content = pane
                buttonTypes.addAll(ButtonType.CANCEL, ButtonType.NEXT)
            }
        }.showAndWait()

        if(!result.isPresent || result.get() == ButtonType.CANCEL) {
            throw SaveLifecycleProvider.AbortException()
        }
    }
}