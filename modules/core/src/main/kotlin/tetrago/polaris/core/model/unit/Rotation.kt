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
import kotlin.math.PI

@Serializable(with = RotationSerializer::class)
data class Rotation(
    val radians: Double
) {
    val degrees get() = radians * 180.0 / PI
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

fun Table.rotation(name: String): Column<Double> = double(name)

fun Column<Double>.byRotation(): ColumnWithTransform<Double, Rotation> {
    return transform({ it.radians }, { it.toRot() })
}

object RotationSerializer : KSerializer<Rotation> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Rotation", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Rotation {
        return decoder.decodeDouble().toRot()
    }

    override fun serialize(encoder: Encoder, value: Rotation) {
        encoder.encodeDouble(value.radians)
    }
}
