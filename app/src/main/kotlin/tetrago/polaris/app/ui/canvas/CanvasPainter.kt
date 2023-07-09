package tetrago.polaris.app.ui.canvas

import javafx.scene.canvas.GraphicsContext
import org.koin.core.context.GlobalContext.get
import kotlin.reflect.KProperty

abstract class CanvasPainter {
    class Holder<T>(private var value: T) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this.value = value
            get().get<CanvasProvider>().repaint()
        }
    }

    protected fun <T> hold(value: T) = Holder(value)

    open fun GraphicsContext.prePaint(width: Double, height: Double) = Unit
    open fun GraphicsContext.paint(width: Double, height: Double) = Unit
}
