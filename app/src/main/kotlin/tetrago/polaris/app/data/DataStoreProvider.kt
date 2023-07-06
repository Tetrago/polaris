package tetrago.polaris.app.data

import io.objectbox.BoxStore
import okio.Path
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.onClose
import java.io.Closeable
import kotlin.reflect.full.primaryConstructor

interface DataStoreProvider : Closeable {
    fun get(): BoxStore

    /**
     * @param   path    Path to save directory; is not module specific.
     */
    fun load(path: Path)

    override fun close() {
        get().close()
    }
}

inline fun <reified T : DataStoreProvider> Module.singleDataStore() {
    single { T::class.primaryConstructor!!.call() } onClose { it?.close() } bind DataStoreProvider::class
}
