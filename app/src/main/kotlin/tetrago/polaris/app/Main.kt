package tetrago.polaris.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import org.pushingpixels.aurora.theming.AuroraSkinDefinition
import org.pushingpixels.aurora.theming.dustSkin
import org.pushingpixels.aurora.window.AuroraApplicationScope
import org.pushingpixels.aurora.window.auroraApplication
import tetrago.polaris.app.koin.ModuleKoinContext
import tetrago.polaris.app.save.SaveFile
import tetrago.polaris.app.save.SaveReader
import tetrago.polaris.app.ui.MainWindow
import tetrago.polaris.app.ui.canvas.BackgroundPainter
import tetrago.polaris.app.ui.canvas.CanvasPainter
import tetrago.polaris.app.ui.dialog.LauncherDialog
import tetrago.polaris.app.ui.window.WindowService

fun main() = auroraApplication {
    var file by remember { mutableStateOf<SaveFile?>(null) }
    if(file != null) {
        MainApplication(dustSkin(), file!!)
    } else {
        LauncherDialog(dustSkin()) { file = it }
    }
}

@Composable
fun AuroraApplicationScope.MainApplication(skin: AuroraSkinDefinition, file: SaveFile) {
    DisposableEffect(Unit) {
        ModuleKoinContext.push()

        val reader = SaveReader(file)
        reader.read()

        loadKoinModules(module {
            single { BackgroundPainter() } bind CanvasPainter::class
            single { WindowService() }
        })

        reader.init()

        onDispose {
            ModuleKoinContext.pop()
        }
    }

    MainWindow(skin)
}
