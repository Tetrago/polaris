package tetrago.polaris.core.save

import javafx.scene.layout.Pane
import org.jetbrains.exposed.sql.SchemaUtils
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.save.SaveDataProvider
import tetrago.polaris.app.ui.Loader
import tetrago.polaris.core.CoreModule
import tetrago.polaris.core.model.Bodies
import tetrago.polaris.core.model.Species
import tetrago.polaris.core.model.SpeciesTable
import tetrago.polaris.core.model.Systems
import tetrago.polaris.core.ui.controller.NewSavePaneController

class SaveData : SaveDataProvider {
    private lateinit var controller: NewSavePaneController

    override fun loadConfig(): Pane? {
        return Loader.loadFxml<NewSavePaneController>("new_save_pane.fxml", ModuleLoader.get<CoreModule>()).let {
            controller = it.second
            it.first
        }
    }

    override fun initialize() {
        SchemaUtils.create(Bodies, SpeciesTable, Systems)

        Species.new {
            name = controller.speciesName.text
        }
    }
}
