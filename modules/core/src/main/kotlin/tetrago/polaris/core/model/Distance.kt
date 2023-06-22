package tetrago.polaris.core.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.dao.ColumnWithTransform
import org.jetbrains.exposed.sql.Column
import tetrago.polaris.core.model.Body.Companion.transform

@Serializable(with = DistanceSerializer::class)
data class Distance(val kilometers: Long) {
    val meters get() = kilometers * 1000L
}

enum class DistanceUnit {
    KILOMETERS,
    METERS
}

fun Number.toDst(unit: DistanceUnit = DistanceUnit.KILOMETERS): Distance
{
    return when(unit) {
        DistanceUnit.KILOMETERS -> Distance(toLong())
        DistanceUnit.METERS -> Distance(toLong() / 1000L)
    }
}

fun Column<Long>.byDistance(): ColumnWithTransform<Long, Distance> {
    return transform({ it.kilometers }, { it.toDst() })
}

object DistanceSerializer : KSerializer<Distance> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Distance", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Distance {
        return decoder.decodeLong().toDst()
    }

    override fun serialize(encoder: Encoder, value: Distance) {
        encoder.encodeLong(value.kilometers)
    }
}
