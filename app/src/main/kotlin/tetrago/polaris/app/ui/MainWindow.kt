package tetrago.polaris.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import org.koin.dsl.module
import org.pushingpixels.aurora.theming.AuroraSkinDefinition
import org.pushingpixels.aurora.window.AuroraApplicationScope
import org.pushingpixels.aurora.window.AuroraWindow
import tetrago.polaris.app.koin.getModuleKoin
import tetrago.polaris.app.ui.canvas.CanvasProvider
import tetrago.polaris.app.ui.canvas.MainCanvas
import tetrago.polaris.app.ui.toolbar.ToolbarProvider

@Composable
fun AuroraApplicationScope.MainWindow(skin: AuroraSkinDefinition) {
    val toolbars: List<ToolbarProvider> = remember { getModuleKoin().getAll() }
    val canvas = remember { MainCanvas() }

    getModuleKoin().loadModules(listOf(module {
        single<CanvasProvider> { canvas }
    }))

    AuroraWindow(
        skin = skin,
        state = rememberWindowState(width = 1280.dp, height = 720.dp),
        onCloseRequest = ::exitApplication,
        title = "Polaris",
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row {
                toolbars.forEach { it.build().project() }
            }

            Row(modifier = Modifier.weight(1f)) {
                canvas.paint()
            }
        }
    }
}
