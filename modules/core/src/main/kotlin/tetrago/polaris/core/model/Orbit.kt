package tetrago.polaris.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.ColumnWithTransform
import org.jetbrains.exposed.sql.Column
import tetrago.polaris.core.model.Body.Companion.transform

@Serializable
data class Orbit(
    val apoapsis: Distance,
    val periapsis: Distance,
    val path: Rotation,
    val offset: Rotation
)

fun Column<String>.byOrbit(): ColumnWithTransform<String, Orbit> {
    return transform({ Json.encodeToString(it) }, { Json.decodeFromString(it) })
}
