package tetrago.polaris.app.save

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.slf4j.LoggerFactory
import tetrago.polaris.app.config.Configuration

object SaveLoader {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private fun parse(file: Path): SaveFile? {
        if(!FileSystem.SYSTEM.exists(file.parent!!.resolve(file.name.replace("json", "db")))) {
            logger.debug("Could not find database for save file `{}`", file)
            return null
        }

        logger.debug("Found save file `{}`", file)
        return SaveFile(file.name.removeSuffix(".json"))
    }

    val saveFiles: List<SaveFile> get() = FileSystem.SYSTEM.list(Configuration.savesPath)
        .filter { it.name.endsWith(".json") }
        .mapNotNull { parse(it) }
        .toList()
}
