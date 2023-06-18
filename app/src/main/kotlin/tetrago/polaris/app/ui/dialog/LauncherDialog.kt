package tetrago.polaris.app.ui.dialog

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.ListCell
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.paint.Color
import javafx.stage.Stage
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.module.ModuleReference
import tetrago.polaris.app.save.SaveFile
import tetrago.polaris.app.save.SaveLoader
import tetrago.polaris.app.ui.Loader
import tetrago.polaris.app.ui.controller.LauncherController
import java.io.File

class LauncherDialog(private val moduleLoader: ModuleLoader) : Stage() {
    private val saveLoader = SaveLoader(File("saves"))

    private val controller: LauncherController
    private val saves = FXCollections.observableArrayList(saveLoader.saveFiles)
    private val modules = FXCollections.observableArrayList<ModuleReference>()

    init {
        title = "Polaris"
        onCloseRequest = EventHandler { Platform.exit() }

        Loader.loadFxml<LauncherController>("launcher.fxml").also {
            scene = Scene(it.first)
            controller = it.second
        }

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
                modules.clear()
                modules.addAll(moduleLoader.resolve(new.enabledModuleIds))
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
            close()
        }
    }
}