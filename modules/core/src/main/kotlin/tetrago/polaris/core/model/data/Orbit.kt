package tetrago.polaris.core.model.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.ColumnWithTransform
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import tetrago.polaris.core.model.Body.Companion.transform
import tetrago.polaris.core.model.unit.Distance
import tetrago.polaris.core.model.unit.Rotation

@Serializable
data class Orbit(
    val apoapsis: Distance,
    val periapsis: Distance,
    val path: Rotation,
    val offset: Rotation
)

fun Table.orbit(name: String): Column<String> = text(name)

fun Column<String>.byOrbit(): ColumnWithTransform<String, Orbit> {
    return transform({ Json.encodeToString(it) }, { Json.decodeFromString(it) })
}
