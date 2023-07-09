package tetrago.polaris.core.data.unit

import io.objectbox.converter.PropertyConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = MassSerializer::class)
data class Mass(
    val tonnes: Double
) {
    val kilograms get() = tonnes * 1000.0

    fun toDouble() = tonnes
}

enum class MassUnit {
    TONNES
}

fun Number.toMass(unit: MassUnit = MassUnit.TONNES): Mass {
    return when(unit) {
        MassUnit.TONNES -> Mass(toDouble())
    }
}

class MassConverter : PropertyConverter<Mass?, Double?> {
    override fun convertToEntityProperty(databaseValue: Double?): Mass? = databaseValue?.toMass()
    override fun convertToDatabaseValue(entityProperty: Mass?): Double? = entityProperty?.toDouble()
}

object MassSerializer : KSerializer<Mass> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Mass", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Mass {
        return decoder.decodeDouble().toMass()
    }

    override fun serialize(encoder: Encoder, value: Mass) {
        encoder.encodeDouble(value.toDouble())
    }
}
