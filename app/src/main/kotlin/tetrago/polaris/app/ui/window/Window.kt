package tetrago.polaris.app.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import org.pushingpixels.aurora.theming.AuroraSkinDefinition
import org.pushingpixels.aurora.theming.marinerSkin
import org.pushingpixels.aurora.window.AuroraApplicationScope
import org.pushingpixels.aurora.window.AuroraWindow
import org.pushingpixels.aurora.window.AuroraWindowScope

abstract class Window(
    private val width: Int,
    private val height: Int,
    private val title: String
) {
    var isOpen = mutableStateOf(false)

    @Composable
    fun draw(scope: AuroraApplicationScope, skin: AuroraSkinDefinition) {
        val state = rememberWindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(width.dp, height.dp)
        )

        scope.AuroraWindow(
            skin = skin,
            title = title,
            state = state,
            onCloseRequest = { isOpen.value = false },
            resizable = false
        ) {
            display()
        }
    }

    @Composable
    protected abstract fun display()
}
