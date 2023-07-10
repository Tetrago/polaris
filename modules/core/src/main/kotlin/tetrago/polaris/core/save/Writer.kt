package tetrago.polaris.core.save

import javafx.scene.layout.Pane
import org.koin.core.annotation.Single
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.save.SaveDataWriter
import tetrago.polaris.app.ui.Loader
import tetrago.polaris.core.CoreModule
import tetrago.polaris.core.generator.Generators
import tetrago.polaris.core.ui.controller.NewSavePaneController
import kotlin.random.Random

@Single
class Writer : SaveDataWriter {
    private lateinit var controller: NewSavePaneController

    override fun loadConfig(): Pane {
        return Loader.loadFxml<NewSavePaneController>("new_save_pane.fxml", ModuleLoader.get<CoreModule>()).let {
            controller = it.second

            controller.speciesName.text = "Human"

            it.first
        }
    }

    override fun initialize(random: Random) {
        loadKoinModules(module {
            single { Generators(random) }
        })
    }
}
