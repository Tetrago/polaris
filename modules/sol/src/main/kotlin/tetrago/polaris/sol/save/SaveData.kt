package tetrago.polaris.sol.save

import javafx.scene.layout.Pane
import tetrago.polaris.app.save.SaveDataProvider
import tetrago.polaris.core.model.*
import kotlin.math.PI
import kotlin.math.pow
import kotlin.random.Random

class SaveData : SaveDataProvider {
    override fun loadConfig(): Pane? = null

    override fun initialize() {
        System.new {
            name = "Sol"
        }.let { sol ->
            Body.new {
                system = sol
                name = "Sun"
                radius = 696340.toDst()
                orbit = Orbit(0.toDst(), 0.toDst(), 0.toRot(), 0.toRot())
            }.let { sun ->
                Body.new {
                    system = sol
                    parent = sun
                    name = "Mercury"
                    radius = 2440.toDst()
                    orbit = Orbit(
                        69.8.pow(6).toDst(),
                        46.0.pow(6).toDst(),
                        0.toRot(),
                        Random.nextDouble(PI).toRot()
                    )
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Venus"
                    radius = 6052.toDst()
                    orbit = Orbit(
                        108.9.pow(6).toDst(),
                        107.5.pow(6).toDst(),
                        0.toRot(),
                        Random.nextDouble(PI).toRot()
                    )
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Earth"
                    radius = 6378.toDst()
                    orbit = Orbit(
                        152.1.pow(6).toDst(),
                        147.1.pow(6).toDst(),
                        0.toRot(),
                        Random.nextDouble(PI).toRot()
                    )
                }.let { earth ->
                    Body.new {
                        system = sol
                        parent = earth
                        name = "Moon"
                        radius = 1737.toDst()
                        orbit = Orbit(404920.toDst(), 367966.toDst(), 0.toRot(), 0.toRot())
                    }
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Mars"
                    radius = 3390.toDst()
                    orbit = Orbit(
                        249.3.pow(6).toDst(),
                        206.7.pow(6).toDst(),
                        0.toRot(),
                        Random.nextDouble(PI).toRot()
                    )
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Jupiter"
                    radius = 69911.toDst()
                    orbit = Orbit(
                        816.4.pow(6).toDst(),
                        740.6.pow(6).toDst(),
                        0.toRot(),
                        Random.nextDouble(PI).toRot()
                    )
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Saturn"
                    radius = 58232.toDst()
                    orbit = Orbit(
                        1506.5.pow(6).toDst(),
                        1357.6.pow(6).toDst(),
                        0.toRot(),
                        Random.nextDouble(PI).toRot()
                    )
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Uranus"
                    radius = 25362.toDst()
                    orbit = Orbit(
                        3001.4.pow(6).toDst(),
                        2732.7.pow(6).toDst(),
                        0.toRot(),
                        Random.nextDouble(PI).toRot()
                    )
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Neptune"
                    radius = 24622.toDst()
                    orbit = Orbit(
                        7375.9.pow(6).toDst(),
                        4436.8.pow(6).toDst(),
                        0.toRot(),
                        Random.nextDouble(PI).toRot()
                    )
                }
            }
        }
    }
}