package tetrago.polaris.app.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import com.sksamuel.hoplite.addResourceSource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

val Configuration = ConfigLoaderBuilder.default()
    .addResourceSource("/tetrago/polaris/app/config/config.toml")
    .addFileSource("config.toml", optional = true)
    .build()
    .loadConfigOrThrow<Config>()

data class Config(
    val moduleDirectory: String,
    val saveDirectory: String
) {
    val modulesPath: Path by lazy {
        moduleDirectory.toPath().also {
            FileSystem.SYSTEM.createDirectory(it)
        }
    }

    val savesPath: Path by lazy {
        saveDirectory.toPath().also {
            FileSystem.SYSTEM.createDirectory(it)
        }
    }
}
