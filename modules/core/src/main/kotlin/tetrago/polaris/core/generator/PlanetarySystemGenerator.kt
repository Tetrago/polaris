package tetrago.polaris.core.generator

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tetrago.polaris.core.data.Body
import tetrago.polaris.core.data.BodyTypeRegistry
import tetrago.polaris.core.data.PlanetarySystem
import tetrago.polaris.core.data.PlanetarySystemRegistry
import tetrago.polaris.core.data.model.Orbit
import tetrago.polaris.core.data.unit.toDst
import tetrago.polaris.core.data.unit.toRot
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class PlanetarySystemGenerator(private val random: Random) : PlanetarySystemProvider, KoinComponent {
    private val generators: Generators by inject()
    private val bodyTypeRegistry: BodyTypeRegistry by inject()

    override fun generate(): PlanetarySystem {
        val system = PlanetarySystem(name = generators.body.next())

        val root = random.nextFloat().let {
            when {
                it < 0.1f -> generateAnchored(system, 3)
                it < 0.3f -> generateAnchored(system, 2)
                else -> generateSystem(generateStar(system))
            }
        }

        system.bodies.add(root)
        return system
    }

    private fun generateStar(system: PlanetarySystem) = Body(
        name = generators.body.next(),
        radius = (40 * random.nextDouble() + 0.8).times(700000).toDst()
    ).also {
        it.system.target = system
        system.bodies.add(it)
    }

    private fun generateBody(system: PlanetarySystem) = Body(
        name = generators.body.next(),
        radius = (0.06 * random.nextDouble() + 0.01).times(300_000).toDst()
    ).also {
        it.system.target = system
        system.bodies.add(it)
    }

    private fun generateAnchored(system: PlanetarySystem, n: Int): Body {
        val anchor = Body().also { it.type.target = bodyTypeRegistry.anchor }

        repeat(n) {
            generateSystem(generateStar(system).apply {
                val dst = random.nextDouble(radius.toDouble() * 4, radius.toDouble() * 40).toDst()
                orbit = Orbit(
                    apoapsis = dst,
                    periapsis = dst,
                    eccentricity = 0.0,
                    skew = 0.toRot(),
                    offset = Random.nextDouble(0.0, 2 * PI).toRot()
                )

                parent.target = anchor
            })
        }

        return anchor
    }

    private fun generateSystem(parent: Body): Body {
        val numberOfBodies = random.nextInt(3, 12)
        var dst = parent.radius.toDouble()

        repeat(numberOfBodies) {
            dst += random.nextDouble(2.0).times(500_000)

            generateBody(parent.system.target).also {
                it.parent.target = parent
                it.orbit = Orbit(
                    apoapsis = dst.toDst(),
                    periapsis = dst.toDst(),
                    0.0,
                    0.toRot(),
                    random.nextDouble(2 * PI).toRot()
                )
            }
        }

        return parent
    }
}