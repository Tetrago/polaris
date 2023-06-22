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
import tetrago.polaris.core.model.Body.Companion.transform

@Serializable(with = MassSerializer::class)
data class Mass(
    val tonnes: Double
) {
    val kilograms get() = tonnes * 1000.0
}

enum class MassUnit {
    TONNES
}

fun Number.toMass(unit: MassUnit = MassUnit.TONNES): Mass {
    return when(unit) {
        MassUnit.TONNES -> Mass(toDouble())
    }
}

fun Table.mass(name: String): Column<Double> = double(name)

fun Column<Double>.byMass(): ColumnWithTransform<Double, Mass> {
    return transform({ it.tonnes }, { it.toMass() })
}

object MassSerializer : KSerializer<Mass> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Mass", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Mass {
        return decoder.decodeDouble().toMass()
    }

    override fun serialize(encoder: Encoder, value: Mass) {
        encoder.encodeDouble(value.tonnes)
    }
}
