package tetrago.polaris.app.ui.dialog

import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.cell.CheckBoxListCell
import javafx.stage.Stage
import tetrago.polaris.app.ui.Loader
import tetrago.polaris.app.ui.controller.LauncherController
import tetrago.polaris.module.ModuleProvider

class LauncherDialog(private val modules: List<ModuleProvider>) : Stage() {
    val selectedModules: List<ModuleProvider>
        get() = modules.filterIndexed { index, _ -> data[index].get() }

    private val controller: LauncherController
    private val data = modules.map { SimpleBooleanProperty(it.id.startsWith("tetrago.polaris")) }

    init {
        title = "Polaris"
        onCloseRequest = EventHandler { Platform.exit() }

        Loader.loadFxml<LauncherController>("launcher.fxml").also {
            scene = Scene(it.first)
            controller = it.second
        }

        controller.saveList.apply {
            items.add("New")
        }

        controller.moduleList.apply {
            items.addAll(modules.map { it.name })
            cellFactory = CheckBoxListCell.forListView { name ->
                data[modules.indexOfFirst { it.name == name }]
            }
        }

        controller.continueButton.setOnAction {
            close()
        }
    }
}