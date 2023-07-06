package tetrago.polaris.app.save

import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.Pane
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import org.koin.core.Koin
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.slf4j.LoggerFactory
import tetrago.polaris.app.config.Configuration
import tetrago.polaris.app.data.DataStoreProvider
import tetrago.polaris.app.data.RegistryProvider
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.module.ModuleProvider
import java.util.*
import kotlin.random.Random

class SaveWriter(
    private val name: String,
    private val modules : List<ModuleProvider>
) {
    class AbortException : Exception()

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    val uuid = UUID.randomUUID()!!.also {
        logger.debug("Found UUID for save `{}`: {}", name, it)
    }

    val directory by lazy {
        Configuration.savesPath.resolve(uuid.toString()).also {
            FileSystem.SYSTEM.createDirectories(it)
        }
    }

    fun writeSaveDescription() {
        val desc = SaveDescription(name, modules.map { it.id })
        FileSystem.SYSTEM.write(directory.resolve("save.json")) {
            writeUtf8(Json.encodeToString(desc))
        }

        logger.info("Wrote save description for save `{}`", name)
    }

    fun writeSaveData(seed: Int): Boolean {
        startKoin { }

        return try {
            ModuleLoader.orderModules(modules).forEach { loadModule(seed, it) }

            true
        } catch(_: AbortException) {
            false
        } finally {
            stopKoin()
        }
    }

    inline fun <reified T : Any> Koin.getAll(module: ModuleProvider): List<T> {
        return getAll<T>().filter { it.javaClass.classLoader == ModuleLoader.moduleMap[module] }
    }

    private fun loadModule(seed: Int, module: ModuleProvider) {
        loadKoinModules(module.modules)

        get().getAll<DataStoreProvider>(module).forEach { it.load(directory) }
        get().getAll<RegistryProvider>(module).forEach { it.create() }

        get().getAll<SaveDataWriter>(module).forEach { writer ->
            writer.loadConfig()?.let { loadWriterDialog(it) }

            writer.initialize(Random(seed))
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
            throw AbortException()
        }
    }
}
