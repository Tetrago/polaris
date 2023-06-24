package tetrago.polaris.app.save

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.get
import tetrago.polaris.app.config.Configuration
import tetrago.polaris.app.module.ModuleLoader
import java.sql.Connection

class SaveReader(private val save: SaveFile) {
    fun loadDatabase() {
        Database.connect(
            "jdbc:sqlite:${Configuration.savesPath.resolve("${save.uuid}.db")}",
            "org.sqlite.JDBC"
        )
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    fun loadModules(app: KoinApplication) = app.run {
        modules(
            ModuleLoader.modules
            .filter { save.enabledModuleIds.contains(it.id) }
            .map { it.modules }
            .flatten())
    }

    fun loadSaveData() {
        get().getAll<SaveDataLoader>().forEach { it.load() }
    }
}