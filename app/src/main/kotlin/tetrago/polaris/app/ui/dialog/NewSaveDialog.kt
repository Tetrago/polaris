package tetrago.polaris.app.ui.dialog

import io.github.serpro69.kfaker.Faker
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.scene.control.cell.CheckBoxListCell
import okio.FileSystem
import okio.Path.Companion.toPath
import org.slf4j.LoggerFactory
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.save.SaveWriter
import tetrago.polaris.app.ui.controller.NewSaveController
import kotlin.math.absoluteValue
import kotlin.random.Random

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
            val modules = ModuleLoader.modules.zip(moduleBoxes).filter { it.second.get() }.map { it.first }

            SaveWriter(controller.nameField.text, modules).apply {
                writeSaveDescription()

                try {
                    writeSaveData(controller.seedField.text.toInt())
                } catch(_: SaveWriter.AbortException) {
                    FileSystem.SYSTEM.delete("saves/$uuid.json".toPath())
                    FileSystem.SYSTEM.delete("saves/$uuid.db".toPath())
                }
            }

            result = true
            close()
        }

        controller.nameField.apply {
            text = Faker().ancient.primordial()
            textProperty().addListener { _, oldValue, newValue ->
                if(newValue.isEmpty() || newValue.matches(Regex(".*[\\\\\"].*"))) {
                    text = oldValue
                }
            }
        }

        controller.seedField.apply {
            text = Random.nextInt().absoluteValue.toString()
            textProperty().addListener { _, oldValue, newValue ->
                if(newValue?.isEmpty() == true) {
                    text = "0"
                } else if(newValue?.toIntOrNull() == null) {
                    text = oldValue
                } else if(text != "0" && text.startsWith("0")) {
                    text = newValue.toInt().toString()
                } else if(newValue.startsWith("-")) {
                    text = newValue.removePrefix("-")
                }
            }
        }
    }
}
