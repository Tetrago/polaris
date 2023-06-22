package tetrago.polaris.app.save

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path

class SaveFile(description: Path) {
    val name: String
    val enabledModuleIds: List<String>

    init {
        val content = FileSystem.SYSTEM.read(description) { readUtf8() }
        val desc = Json.decodeFromString<SaveDescription>(content)

        name = desc.name
        enabledModuleIds = desc.modules
    }
}
