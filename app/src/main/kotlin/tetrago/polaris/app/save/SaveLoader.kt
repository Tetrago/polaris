package tetrago.polaris.app.save

import okio.FileSystem
import okio.Path
import org.slf4j.LoggerFactory
import tetrago.polaris.app.config.Configuration

object SaveLoader {
    val saveFiles: List<SaveFile> get() = FileSystem.SYSTEM.list(Configuration.savesPath)
        .filter { FileSystem.SYSTEM.exists(it.resolve("save.json")) }
        .map { SaveFile(it) }
        .toList()
}
