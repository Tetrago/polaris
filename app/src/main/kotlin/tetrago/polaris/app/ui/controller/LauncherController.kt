package tetrago.polaris.app.ui.controller

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView

class LauncherController {
    @FXML
    lateinit var saveList: ListView<String>

    @FXML
    lateinit var moduleList: ListView<String>

    @FXML
    lateinit var continueButton: Button
}