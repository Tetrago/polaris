package tetrago.polaris.app.ui.dialog

import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.scene.control.cell.CheckBoxListCell
import okio.FileSystem
import okio.Path.Companion.toPath
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.save.SaveWriter
import tetrago.polaris.app.ui.controller.NewSaveController

class NewSaveDialog : ResultDialog<NewSaveController, Boolean>("New Save", "new_save.fxml") {
    private val moduleBoxes = ModuleLoader.modules.map {
        SimpleBooleanProperty(it.id.startsWith("tetrago.polaris"))
    }

    init {
        onCloseRequest = EventHandler {
            result = false
            close()
        }

        controller.modulesList.apply {
            items.addAll(ModuleLoader.modules.map { it.name })
            cellFactory = CheckBoxListCell.forListView { name ->
                moduleBoxes[ModuleLoader.modules.indexOfFirst { it.name == name }]
            }
        }

        controller.cancelButton.setOnAction {
            result = false
            close()
        }

        controller.continueButton.setOnAction {
            if(!validate()) return@setOnAction

            val modules = ModuleLoader.modules.zip(moduleBoxes).filter { it.second.get() }.map { it.first }

            SaveWriter("saves".toPath(), controller.nameField.text, modules).apply {
                writeSaveDescription()

                try {
                    writeSaveData()
                } catch(e: IllegalStateException) {
                    FileSystem.SYSTEM.delete("saves/$uuid.json".toPath())
                    FileSystem.SYSTEM.delete("saves/$uuid.db".toPath())
                }
            }

            result = true
            close()
        }
    }

    private fun validate(): Boolean {
        return controller.nameField.text.isNotEmpty()
    }
}