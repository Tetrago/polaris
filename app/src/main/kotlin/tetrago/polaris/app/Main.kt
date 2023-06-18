package tetrago.polaris.app

import javafx.application.Application.launch
import org.koin.core.context.startKoin
import org.koin.dsl.module
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.ui.MainApplication
import tetrago.polaris.app.ui.window.WindowService
import tetrago.polaris.app.ui.window.WindowServiceProvider
import java.io.File

fun main() {
    launch(MainApplication::class.java)
}
