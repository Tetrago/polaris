package tetrago.polaris.app.ui

import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.ui.canvas.CanvasProvider
import tetrago.polaris.app.ui.dialog.LauncherDialog
import tetrago.polaris.app.ui.window.WindowService
import tetrago.polaris.app.ui.window.WindowServiceProvider
import java.io.File

class MainApplication : Application(), KoinComponent {
    override fun start(primaryStage: Stage?) {
        val loader = ModuleLoader(File("modules"))

        val launcher = LauncherDialog(loader.modules)
        launcher.showAndWait()

        startKoin {
            modules(module {
                single<WindowServiceProvider> { WindowService() }
            })

            modules(launcher.selectedModules.map { it.modules }.flatten())
        }

        val window = MainWindow(primaryStage!!)

        loadKoinModules(module {
            single<CanvasProvider> { window.canvas }
        })

        primaryStage.show()
    }
}