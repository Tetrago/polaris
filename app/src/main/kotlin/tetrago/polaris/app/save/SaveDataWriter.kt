package tetrago.polaris.app.save

import javafx.scene.layout.Pane
import kotlin.random.Random

interface SaveDataWriter {
    fun loadConfig(): Pane?

    fun initialize(random: Random)
}
