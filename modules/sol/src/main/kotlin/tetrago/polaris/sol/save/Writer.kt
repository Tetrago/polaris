package tetrago.polaris.sol.save

import androidx.compose.runtime.Composable
import org.koin.core.component.get
import org.koin.core.component.inject
import tetrago.polaris.app.koin.ModuleKoinComponent
import tetrago.polaris.app.save.SaveDataWriter
import tetrago.polaris.core.data.Body
import tetrago.polaris.core.data.BodyTypeRegistry
import tetrago.polaris.core.data.PlanetarySystem
import tetrago.polaris.core.data.PlanetarySystemRegistry
import tetrago.polaris.core.data.model.Orbit
import tetrago.polaris.core.data.unit.toDst
import tetrago.polaris.core.data.unit.toRot
import kotlin.math.PI
import kotlin.math.pow
import kotlin.random.Random

class Writer : SaveDataWriter, ModuleKoinComponent {
    private val bodyTypeRegistry: BodyTypeRegistry by inject()

    private lateinit var system: PlanetarySystem

    @Composable
    override fun display() = Unit

    override fun initialize(random: Random) {
        system = PlanetarySystem(name = "Sol")

        Body(
            name = "Sun",
            radius = 696340.toDst()
        ).apply {
            type.target = bodyTypeRegistry.star

            addTerrestrials()
            addGiants()

            this@Writer.system.bodies.add(this)
        }

        get<PlanetarySystemRegistry>().store.put(system)
    }

    private fun Body.body(name: String, block: Body.() -> Unit) = Body(name = name).apply {
        parent.target = this@body
        block()

        this@Writer.system.bodies.add(this)
    }

    private fun Double.exp(v: Int) = this@exp * 10.0.pow(v)

    private fun Body.addTerrestrials() {
        body("Mercury") {
            radius = 2440.toDst()
            orbit = Orbit(
                69.8.exp(6).toDst(),
                46.0.exp(6).toDst(),
                0.206,
                0.toRot(),
                Random.nextDouble(PI).toRot()
            )
            type.target = bodyTypeRegistry.terrestrial
        }

        body("Venus") {
            radius = 6052.toDst()
            orbit = Orbit(
                108.9.exp(6).toDst(),
                107.5.exp(6).toDst(),
                0.007,
                0.toRot(),
                Random.nextDouble(PI).toRot()
            )
            type.target = bodyTypeRegistry.terrestrial
        }

        body("Earth") {
            radius = 6378.toDst()
            orbit = Orbit(
                152.1.exp(6).toDst(),
                147.1.exp(6).toDst(),
                0.017,
                0.toRot(),
                Random.nextDouble(PI).toRot()
            )
            type.target = bodyTypeRegistry.terrestrial

            body("Moon") {
                radius = 1737.toDst()
                orbit = Orbit(404920.toDst(), 367966.toDst(), 0.055, 0.toRot(), 0.toRot())
                type.target = bodyTypeRegistry.terrestrial
            }
        }

        body("Mars") {
            radius = 3390.toDst()
            orbit = Orbit(
                249.3.exp(6).toDst(),
                206.7.exp(6).toDst(),
                0.094,
                0.toRot(),
                Random.nextDouble(PI).toRot()
            )
            type.target = bodyTypeRegistry.terrestrial
        }
    }

    private fun Body.addGiants() {
        body("Jupiter") {
            radius = 69911.toDst()
            orbit = Orbit(
                816.4.exp(6).toDst(),
                740.6.exp(6).toDst(),
                0.049,
                0.toRot(),
                Random.nextDouble(PI).toRot()
            )
            type.target = bodyTypeRegistry.gasGiant
        }

        body("Saturn") {
            radius = 58232.toDst()
            orbit = Orbit(
                1506.5.exp(6).toDst(),
                1357.6.exp(6).toDst(),
                0.052,
                0.toRot(),
                Random.nextDouble(PI).toRot()
            )
            type.target = bodyTypeRegistry.gasGiant
        }

        body("Uranus") {
            radius = 25362.toDst()
            orbit = Orbit(
                3001.4.exp(6).toDst(),
                2732.7.exp(6).toDst(),
                0.047,
                0.toRot(),
                Random.nextDouble(PI).toRot()
            )
            type.target = bodyTypeRegistry.iceGiant
        }

        body("Neptune") {
            radius = 24622.toDst()
            orbit = Orbit(
                7375.9.exp(6).toDst(),
                4436.8.exp(6).toDst(),
                0.010,
                0.toRot(),
                Random.nextDouble(PI).toRot()
            )
            type.target = bodyTypeRegistry.iceGiant
        }
    }
}
