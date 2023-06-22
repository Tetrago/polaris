package tetrago.polaris.app.save

import javafx.scene.layout.Pane

interface SaveDataProvider {
    fun loadConfig(): Pane?

    fun initialize()
}
