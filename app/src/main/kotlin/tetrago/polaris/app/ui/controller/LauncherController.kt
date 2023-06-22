package tetrago.polaris.app.ui.controller

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import tetrago.polaris.app.module.ModuleReference
import tetrago.polaris.app.save.SaveFile

class LauncherController {
    @FXML
    lateinit var saveList: ListView<SaveFile>

    @FXML
    lateinit var moduleList: ListView<ModuleReference>

    @FXML
    lateinit var loadButton: Button

    @FXML
    lateinit var newButton: Button
}
