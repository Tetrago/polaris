package tetrago.polaris.core.data.unit

import io.objectbox.converter.PropertyConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = DensitySerializer::class)
data class Density(
    val kilogramsPerCubicMeter: Double
) {
    fun toDouble() = kilogramsPerCubicMeter
}

enum class DensityUnit {
    KILOGRAMS_PER_CUBIC_METER
}

fun Number.toDensity(unit: DensityUnit = DensityUnit.KILOGRAMS_PER_CUBIC_METER): Density {
    return when(unit) {
        DensityUnit.KILOGRAMS_PER_CUBIC_METER -> Density(toDouble())
    }
}

class DensityConverter : PropertyConverter<Density?, Double?> {
    override fun convertToEntityProperty(databaseValue: Double?): Density? = databaseValue?.toDensity()
    override fun convertToDatabaseValue(entityProperty: Density?): Double? = entityProperty?.toDouble()
}

object DensitySerializer : KSerializer<Density> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Density", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Density {
        return decoder.decodeDouble().toDensity()
    }

    override fun serialize(encoder: Encoder, value: Density) {
        encoder.encodeDouble(value.toDouble())
    }
}
