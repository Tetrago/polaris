package tetrago.polaris.app.save

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import org.koin.core.component.getScopeName
import tetrago.polaris.app.config.Configuration

class SaveFile(val directory: Path) {
    val name: String
    val enabledModuleIds: List<String>

    init {
        val content = FileSystem.SYSTEM.read(directory.resolve("save.json")) { readUtf8() }
        Json.decodeFromString<SaveDescription>(content).also {
            name = it.name
            enabledModuleIds = it.modules
        }
    }
}
