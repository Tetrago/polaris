package tetrago.polaris.app

import javafx.application.Application.launch
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.ui.MainApplication
import tetrago.polaris.app.ui.window.WindowModule
import java.io.File

fun main() {
    startKoin {
        modules(WindowModule().module)

        val loader = ModuleLoader(File("modules"))
        modules(loader.loadAll())
    }

    launch(MainApplication::class.java)
}
