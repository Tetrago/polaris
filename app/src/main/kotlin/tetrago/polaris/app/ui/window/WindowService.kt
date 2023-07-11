package tetrago.polaris.app.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.koin.core.annotation.Single
import org.koin.core.context.GlobalContext.get
import org.pushingpixels.aurora.theming.AuroraSkinDefinition
import org.pushingpixels.aurora.window.AuroraApplicationScope
import kotlin.reflect.KClass

@Single
class WindowService {
    private val windows: List<Window> by lazy { get().getAll() }

    inline fun <reified T : Window> open() = open(T::class)
    inline fun <reified T : Window> close() = close(T::class)

    fun <T : Window> open(cls: KClass<T>) {
        windows.single { it::class == cls }.isOpen.value = true
    }

    fun <T : Window> close(cls: KClass<T>) {
        windows.single { it::class == cls }.isOpen.value = false
    }

    @Composable
    fun draw(scope: AuroraApplicationScope, skin: AuroraSkinDefinition) {
        for(window in windows) {
            val isOpen by remember { window.isOpen }
            if(isOpen) {
                window.draw(scope, skin)
            }
        }
    }
}
