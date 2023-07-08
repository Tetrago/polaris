package tetrago.polaris.app.ui

import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import tetrago.polaris.app.save.SaveReader
import tetrago.polaris.app.ui.canvas.CanvasProvider
import tetrago.polaris.app.ui.dialog.LauncherDialog
import tetrago.polaris.app.ui.window.WindowService

class MainApplication : Application(), KoinComponent {
    companion object {
        val icon by lazy { Image(this::class.java.getResourceAsStream("icon.png")) }
    }

    override fun start(primaryStage: Stage?) {
        val result = LauncherDialog().prompt() ?: return
        val reader = SaveReader(result)

        startKoin { }
        reader.read()

        val window = MainWindow(primaryStage!!)

        loadKoinModules(module {
            single<CanvasProvider> { window.canvas }
            single { WindowService() }
        })

        reader.init()
        primaryStage.show()

        stopKoin()
    }
}
