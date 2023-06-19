package tetrago.polaris.sol.save

import javafx.scene.layout.Pane
import tetrago.polaris.app.save.SaveDataProvider
import tetrago.polaris.core.model.Body
import tetrago.polaris.core.model.System
import tetrago.polaris.core.model.toDistance

class SaveData : SaveDataProvider {
    override fun loadConfig(): Pane? = null

    override fun initialize() {
        System.new {
            name = "Sol"
        }.let { sol ->
            Body.new {
                system = sol
                name = "Sun"
                radius = 696340.toDistance()
            }.let { sun ->
                Body.new {
                    system = sol
                    parent = sun
                    name = "Mercury"
                    radius = 2440.toDistance()
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Venus"
                    radius = 6052.toDistance()
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Earth"
                    radius = 6378.toDistance()
                }.let { earth ->
                    Body.new {
                        system = sol
                        parent = earth
                        name = "Moon"
                        radius = 1737.toDistance()
                    }
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Mars"
                    radius = 3390.toDistance()
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Jupiter"
                    radius = 69911.toDistance()
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Saturn"
                    radius = 58232.toDistance()
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Uranus"
                    radius = 25362.toDistance()
                }

                Body.new {
                    system = sol
                    parent = sun
                    name = "Neptune"
                    radius = 24622.toDistance()
                }
            }
        }
    }
}