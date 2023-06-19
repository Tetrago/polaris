package tetrago.polaris.app.ui.dialog

import javafx.collections.FXCollections
import javafx.scene.control.ListCell
import javafx.scene.paint.Color
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.module.ModuleReference
import tetrago.polaris.app.save.SaveFile
import tetrago.polaris.app.save.SaveLoader
import tetrago.polaris.app.ui.controller.LauncherController

class LauncherDialog : ResultDialog<LauncherController, SaveFile>("Polaris", "launcher.fxml") {
    private val saves = FXCollections.observableArrayList(SaveLoader.saveFiles)
    private val modules = FXCollections.observableArrayList<ModuleReference>()

    init {
        controller.saveList.apply {
            items = saves
            setCellFactory {
                object : ListCell<SaveFile>() {
                    override fun updateItem(item: SaveFile?, empty: Boolean) {
                        super.updateItem(item, empty)
                        text = item?.name
                    }
                }
            }
            selectionModel.selectedItemProperty().addListener { _, _, new ->
                new?.let {
                    modules.clear()
                    modules.addAll(ModuleLoader.resolve(new.enabledModuleIds))
                }
            }
        }

        controller.moduleList.apply {
            items = modules
            isFocusTraversable = false
            setCellFactory {
                object : ListCell<ModuleReference>() {
                    override fun updateItem(item: ModuleReference?, empty: Boolean) {
                        super.updateItem(item, empty)

                        text = item?.provider?.name ?: item?.id
                        textFill = if(item?.provider != null) Color.BLACK else Color.RED
                    }
                }
            }
        }

        controller.loadButton.setOnAction {
            close()
        }

        controller.newButton.setOnAction {
            if(NewSaveDialog().prompt() == true) {
                saves.clear()
                saves.addAll(SaveLoader.saveFiles)
            }
        }
    }
}