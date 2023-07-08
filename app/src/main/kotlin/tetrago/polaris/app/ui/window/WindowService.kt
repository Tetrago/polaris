package tetrago.polaris.app.ui.window

import javafx.scene.Scene
import javafx.stage.Stage
import org.koin.core.annotation.Single
import org.koin.mp.KoinPlatform.getKoin

@Single
class WindowService {
    private val windows: List<Window> = getKoin().getAll()
    private val stages = windows.map {
        val stage = Stage()

        stage.scene = Scene(it, 800.0, 600.0)
        stage.title = it.title

        it.tag to stage
    }.toMap()

    fun open(tag: String) {
        stages[tag]?.let {
            if(it.isShowing) {
                it.toFront()
            } else {
                it.show()
            }
        }
    }

    fun close(tag: String) {
        stages[tag]?.hide()
    }
}
