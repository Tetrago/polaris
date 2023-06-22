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
import tetrago.polaris.core.model.System.Companion.transform

@Serializable(with = DensitySerializer::class)
data class Density(
    val kilogramsPerCubicMeter: Double
)

enum class DensityUnit {
    KILOGRAMS_PER_CUBIC_METER
}

fun Number.toDensity(unit: DensityUnit = DensityUnit.KILOGRAMS_PER_CUBIC_METER): Density {
    return when(unit) {
        DensityUnit.KILOGRAMS_PER_CUBIC_METER -> Density(toDouble())
    }
}

fun Table.density(name: String): Column<Double> = double(name)

fun Column<Double>.byDensity(): ColumnWithTransform<Double, Density> {
    return transform({ it.kilogramsPerCubicMeter }, { it.toDensity() })
}

object DensitySerializer : KSerializer<Density> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Density", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Density {
        return decoder.decodeDouble().toDensity()
    }

    override fun serialize(encoder: Encoder, value: Density) {
        encoder.encodeDouble(value.kilogramsPerCubicMeter)
    }
}
