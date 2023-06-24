package tetrago.polaris.app.save

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import tetrago.polaris.app.config.Configuration

class SaveFile(val uuid: String) {
    val name: String
    val enabledModuleIds: List<String>

    init {
        val content = FileSystem.SYSTEM.read(Configuration.savesPath.resolve("$uuid.json")) { readUtf8() }
        val desc = Json.decodeFromString<SaveDescription>(content)

        name = desc.name
        enabledModuleIds = desc.modules
    }
}
