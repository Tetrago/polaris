package tetrago.polaris.app.save

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.buffer
import okio.source
import java.io.File
import java.nio.charset.Charset

class SaveFile(description: File) {
    val name: String
    val enabledModuleIds: List<String>

    init {
        val content = description.source().buffer().readString(Charset.defaultCharset())
        val desc = Json.decodeFromString<SaveDescription>(content)

        name = desc.name
        enabledModuleIds = desc.modules
    }

    fun load(): Save {
        return Save()
    }
}