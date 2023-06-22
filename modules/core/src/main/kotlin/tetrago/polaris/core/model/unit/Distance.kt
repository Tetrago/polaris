package tetrago.polaris.core.model.unit

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.dao.ColumnWithTransform
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Table.Dual.double
import tetrago.polaris.core.model.Body.Companion.transform

@Serializable(with = DistanceSerializer::class)
data class Distance(val kilometers: Double) {
    val meters get() = kilometers * 1000.0
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

fun Table.distance(name: String): Column<Double> = double(name)

fun Column<Double>.byDistance(): ColumnWithTransform<Double, Distance> {
    return transform({ it.kilometers }, { it.toDst() })
}

object DistanceSerializer : KSerializer<Distance> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Distance", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Distance {
        return decoder.decodeDouble().toDst()
    }

    override fun serialize(encoder: Encoder, value: Distance) {
        encoder.encodeDouble(value.kilometers)
    }
}
