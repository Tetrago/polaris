package tetrago.polaris.app.save

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.random.Random

class SaveWriter(private val file: SaveFile) {
    fun write(seed: Int): Boolean {
        startKoin { }

        return try {
            SaveLifecycle().write(file) { Random(seed) }

            true
        } catch(_: SaveLifecycleProvider.AbortException) {
            false
        } finally {
            stopKoin()
        }
    }
}
