package tetrago.polaris.core.data.unit

import io.objectbox.converter.PropertyConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.PI

@Serializable(with = RotationSerializer::class)
data class Rotation(
    val radians: Double
) {
    val degrees get() = radians * 180.0 / PI

    fun toDouble() = radians
}

enum class RotationUnit {
    DEGREES,
    RADIANS
}

fun Number.toRot(unit: RotationUnit = RotationUnit.RADIANS): Rotation {
    return when(unit) {
        RotationUnit.DEGREES -> Rotation(toDouble() * PI / 180.0)
        RotationUnit.RADIANS -> Rotation(toDouble())
    }
}

class RotationConverter : PropertyConverter<Rotation?, Double?> {
    override fun convertToEntityProperty(databaseValue: Double?): Rotation? = databaseValue?.toRot()
    override fun convertToDatabaseValue(entityProperty: Rotation?): Double? = entityProperty?.toDouble()
}

object RotationSerializer : KSerializer<Rotation> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Rotation", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Rotation {
        return decoder.decodeDouble().toRot()
    }

    override fun serialize(encoder: Encoder, value: Rotation) {
        encoder.encodeDouble(value.toDouble())
    }
}
