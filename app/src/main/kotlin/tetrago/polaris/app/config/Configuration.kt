package tetrago.polaris.app.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addFileSource
import com.sksamuel.hoplite.addResourceSource

val Configuration = ConfigLoaderBuilder.default()
    .addResourceSource("/tetrago/polaris/app/config/config.toml")
    .addFileSource("config.toml", optional = true)
    .build()
    .loadConfigOrThrow<Config>()

data class Config(
    val moduleDirectory: String,
    val saveDirectory: String
)
