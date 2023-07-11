package tetrago.polaris.app.save

import androidx.compose.runtime.Composable
import org.pushingpixels.aurora.window.AuroraApplicationScope
import kotlin.random.Random

interface SaveDataWriter {
    @Composable
    fun display()

    fun initialize(random: Random)
}
