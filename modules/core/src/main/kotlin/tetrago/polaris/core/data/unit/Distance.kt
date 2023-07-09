package tetrago.polaris.core.data.unit

import io.objectbox.converter.PropertyConverter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = DistanceSerializer::class)
data class Distance(val kilometers: Double) {
    val meters get() = kilometers * 1000.0

    fun toDouble() = kilometers
}

enum class DistanceUnit {
    KILOMETERS,
    METERS
}

fun Number.toDst(unit: DistanceUnit = DistanceUnit.KILOMETERS): Distance {
    return when(unit) {
        DistanceUnit.KILOMETERS -> Distance(toDouble())
        DistanceUnit.METERS -> Distance(toDouble() / 1000.0)
    }
}

class DistanceConverter : PropertyConverter<Distance?, Double?> {
    override fun convertToEntityProperty(databaseValue: Double?): Distance? = databaseValue?.toDst()
    override fun convertToDatabaseValue(entityProperty: Distance?): Double? = entityProperty?.toDouble()
}

object DistanceSerializer : KSerializer<Distance> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Distance", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Distance {
        return decoder.decodeDouble().toDst()
    }

    override fun serialize(encoder: Encoder, value: Distance) {
        encoder.encodeDouble(value.toDouble())
    }
}
