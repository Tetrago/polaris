package tetrago.polaris.app.save

import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionManager
import org.koin.dsl.koinApplication
import org.slf4j.LoggerFactory
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.module.ModuleProvider
import java.sql.Connection
import java.util.UUID
import kotlin.IllegalStateException

class SaveWriter(
    private val directory: Path,
    private val name: String,
    private val modules : List<ModuleProvider>
) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    val uuid = UUID.randomUUID().also {
        logger.debug("Found UUID for save `{}`: {}", name, it)
    }

    fun writeSaveDescription() {
        val desc = SaveDescription(name, modules.map { it.id })
        FileSystem.SYSTEM.write(directory.resolve("$uuid.json")) {
            writeUtf8(Json.encodeToString(desc))
        }

        logger.info("Wrote save description for save `{}`", name)
    }

    @Throws(IllegalStateException::class)
    fun writeSaveData() {
        val path = directory.resolve("$uuid.db")
        val database = Database.connect("jdbc:sqlite:${path}", "org.sqlite.JDBC")
        database.transactionManager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        ModuleLoader.orderModules(modules).forEach { module ->
            transaction(database) {
                koinApplication {
                    modules(module.modules)

                    koin.getAll<SaveDataProvider>().forEach {
                        it.loadConfig()?.let { pane ->
                            val result = Dialog<ButtonType>().apply {
                                title = "New Save"
                                dialogPane.apply {
                                    content = pane
                                    buttonTypes.addAll(ButtonType.CANCEL, ButtonType.NEXT)
                                }
                            }.showAndWait()

                            if(result.isEmpty || result.get() == ButtonType.CANCEL) {
                                throw IllegalStateException("Save creation aborted")
                            }
                        }

                        it.initialize()
                    }
                }
            }
        }
    }
}