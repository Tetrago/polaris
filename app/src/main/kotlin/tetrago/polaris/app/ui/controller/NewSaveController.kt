package tetrago.polaris.app.ui.controller

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField

class NewSaveController {
    @FXML
    lateinit var cancelButton: Button

    @FXML
    lateinit var continueButton: Button

    @FXML
    lateinit var nameField: TextField

    @FXML
    lateinit var modulesList: ListView<String>
}
