package tetrago.polaris.app.save

import javafx.scene.layout.Pane
import kotlin.random.Random

interface SaveDataProvider {
    fun loadConfig(): Pane?

    fun initialize(random: Random)
}
