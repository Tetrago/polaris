package tetrago.polaris.core.model

enum class DistanceUnit {
    KILOMETERS,
    METERS
}

class Distance(val kilometers: Long) {
    val meters get() = kilometers * 1000L
}

fun <T : Number> T.toDistance(unit: DistanceUnit = DistanceUnit.KILOMETERS): Distance
{
    return when(unit) {
        DistanceUnit.KILOMETERS -> Distance(this.toLong())
        DistanceUnit.METERS -> Distance(this.toLong() / 1000L)
    }
}
