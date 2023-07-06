package tetrago.polaris.core.save

import io.github.serpro69.kfaker.Faker
import javafx.scene.layout.Pane
import org.koin.core.annotation.Single
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.save.SaveDataWriter
import tetrago.polaris.app.ui.Loader
import tetrago.polaris.core.CoreModule
import tetrago.polaris.core.data.AtmosphereRegistry
import tetrago.polaris.core.data.BodyRegistry
import tetrago.polaris.core.data.BodyTypeRegistry
import tetrago.polaris.core.data.DepositRegistry
import tetrago.polaris.core.data.GasRegistry
import tetrago.polaris.core.data.Mineral
import tetrago.polaris.core.data.MineralRegistry
import tetrago.polaris.core.data.PlanetarySystem
import tetrago.polaris.core.data.PlanetarySystemRegistry
import tetrago.polaris.core.data.Species
import tetrago.polaris.core.data.SpeciesRegistry
import tetrago.polaris.core.ui.controller.NewSavePaneController
import kotlin.random.Random

@Single
class Writer : SaveDataWriter {
    private lateinit var controller: NewSavePaneController

    override fun loadConfig(): Pane {
        return Loader.loadFxml<NewSavePaneController>("new_save_pane.fxml", ModuleLoader.get<CoreModule>()).let {
            controller = it.second

            controller.speciesName.text = Faker().ancient.titan()

            it.first
        }
    }

    override fun initialize(random: Random) = Unit
}
