package tetrago.polaris.app.data

import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder
import okio.Path

abstract class DataStore(private val id: String) : DataStoreProvider {
    private lateinit var store: BoxStore

    override fun get(): BoxStore = store

    override fun load(path: Path) {
        store = builder
            .directory(path.resolve(id).toFile())
            .build()
    }

    protected abstract val builder: BoxStoreBuilder
}
