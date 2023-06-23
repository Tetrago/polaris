package tetrago.polaris.core.model.registry

import tetrago.polaris.core.model.BodyType
import tetrago.polaris.core.model.BodyTypes
import tetrago.polaris.module.Holder
import tetrago.polaris.module.Registry

@Registry(BodyTypes::class)
object BodyTypeRegistry {
    @Holder
    val Terrestrial = BodyType("Terrestrial", true, true)

    @Holder
    val GasGiant = BodyType("Gas Giant", false, false)

    @Holder
    val IceGiant = BodyType("Ice Giant", false, false)

    @Holder
    val Asteroid = BodyType("Asteroid", true, false)
}
