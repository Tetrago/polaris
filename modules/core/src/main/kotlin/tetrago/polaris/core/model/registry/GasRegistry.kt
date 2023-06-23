package tetrago.polaris.core.model.registry

import tetrago.polaris.core.model.Gas
import tetrago.polaris.core.model.Gases
import tetrago.polaris.module.Holder
import tetrago.polaris.module.Registry

@Registry(Gases::class)
object GasRegistry {
    @Holder
    val Nitrogen = Gas("Nitrogen", "N2")

    @Holder
    val Oxygen = Gas("Oxygen", "O2")

    @Holder("Water Vapor")
    val WaterVapor = Gas("Water Vapor", "H2O")

    @Holder("Carbon Dioxide")
    val CarbonDioxide = Gas("Carbon Dioxide", "CO2")

    @Holder
    val Argon = Gas("Argon", "Ar2")
}