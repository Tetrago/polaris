package tetrago.polaris.app.save

import kotlinx.serialization.Serializable

@Serializable
data class SaveDescription(
    val name: String,
    val modules: List<String>
)