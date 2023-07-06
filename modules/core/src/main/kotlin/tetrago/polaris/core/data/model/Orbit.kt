package tetrago.polaris.core.data.model

import io.objectbox.converter.PropertyConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tetrago.polaris.core.data.unit.Distance
import tetrago.polaris.core.data.unit.Rotation

@Serializable
data class Orbit(
    val apoapsis: Distance,
    val periapsis: Distance,
    val path: Rotation,
    val offset: Rotation
)

class OrbitConverter : PropertyConverter<Orbit?, String?> {
    override fun convertToEntityProperty(databaseValue: String?): Orbit? {
        return databaseValue?.let { Json.decodeFromString(it) }
    }

    override fun convertToDatabaseValue(entityProperty: Orbit?): String? {
        return entityProperty?.let { Json.encodeToString(it) }
    }
}
