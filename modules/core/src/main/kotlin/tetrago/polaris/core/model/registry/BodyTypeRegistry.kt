package tetrago.polaris.core.model.registry

import tetrago.polaris.core.model.BodyType
import tetrago.polaris.core.model.BodyTypes
import tetrago.polaris.module.Holder
import tetrago.polaris.module.Registry

@Registry(BodyTypes::class)
object BodyTypeRegistry {
    @Holder
    lateinit var Star: BodyType

    @Holder
    lateinit var Terrestrial: BodyType

    @Holder("Gas Giant")
    lateinit var GasGiant: BodyType

    @Holder("Ice Giant")
    lateinit var IceGiant: BodyType

    @Holder
    lateinit var Asteroid: BodyType

    fun create() {
        Star = BodyType("Star", solid = false, colonizable = false)
        Terrestrial = BodyType("Terrestrial", solid = true, colonizable = true)
        GasGiant = BodyType("Gas Giant", solid = false, colonizable = false)
        IceGiant = BodyType("Ice Giant", solid = false, colonizable = false)
        Asteroid = BodyType("Asteroid", solid = true, colonizable = false)
    }
}
