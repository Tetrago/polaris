package tetrago.polaris.core.ui.window

import androidx.compose.runtime.Composable
import org.koin.core.annotation.Single
import org.pushingpixels.aurora.window.AuroraWindowScope
import tetrago.polaris.app.ui.window.Window

@Single
class InfrastructureWindow : Window(800, 600, "Infrastructure") {
    @Composable
    override fun display() {
    }
}
