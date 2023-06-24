package tetrago.polaris.core.model.registry

import tetrago.polaris.core.model.Mineral
import tetrago.polaris.core.model.Minerals
import tetrago.polaris.module.Holder
import tetrago.polaris.module.Registry

@Registry(Minerals::class)
object MineralRegistry {
    @Holder
    lateinit var Titanium: Mineral

    @Holder
    lateinit var Aluminum: Mineral

    fun create() {
        Titanium = Mineral("Titanium")
        Aluminum = Mineral("Aluminum")
    }
}
