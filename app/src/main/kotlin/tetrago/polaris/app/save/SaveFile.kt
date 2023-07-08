package tetrago.polaris.app.save

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import tetrago.polaris.app.config.Configuration
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.module.ModuleProvider
import java.util.UUID

class SaveFile private constructor() {
    lateinit var directory: Path
        private set

    lateinit var description: SaveDescription
        private set

    constructor(directory: Path) : this() {
        val content = FileSystem.SYSTEM.read(directory.resolve("save.json")) { readUtf8() }

        this.directory = directory
        description = Json.decodeFromString<SaveDescription>(content)
    }

    constructor(name: String, modules: List<ModuleProvider>) : this() {
        directory = Configuration.savesPath.resolve(UUID.randomUUID().toString())
        description = SaveDescription(name, modules.map { it.id })

        FileSystem.SYSTEM.createDirectories(directory)

        FileSystem.SYSTEM.write(directory.resolve("save.json")) {
            writeUtf8(Json.encodeToString(description))
        }
    }

    val modules get() = description.modules.map { id -> ModuleLoader.modules.single { it.id == id } }
}
