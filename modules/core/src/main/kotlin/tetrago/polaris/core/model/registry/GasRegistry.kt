package tetrago.polaris.core.model.registry

import tetrago.polaris.core.model.Gas
import tetrago.polaris.core.model.Gases
import tetrago.polaris.module.Holder
import tetrago.polaris.module.Registry

@Registry(Gases::class)
object GasRegistry {
    @Holder
    lateinit var Nitrogen: Gas

    @Holder
    lateinit var Oxygen: Gas

    @Holder("Water Vapor")
    lateinit var WaterVapor: Gas

    @Holder("Carbon Dioxide")
    lateinit var CarbonDioxide: Gas

    @Holder
    lateinit var Argon: Gas

    fun create() {
        Nitrogen = Gas("Nitrogen", "N2")
        Oxygen = Gas("Oxygen", "O2")
        WaterVapor = Gas("Water Vapor", "H2O")
        CarbonDioxide = Gas("Carbon Dioxide", "CO2")
        Argon = Gas("Argon", "Ar2")
    }
}
