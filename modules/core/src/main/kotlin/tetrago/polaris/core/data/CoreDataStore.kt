package tetrago.polaris.core.data

import io.objectbox.BoxStoreBuilder
import org.koin.core.annotation.Single
import tetrago.polaris.app.data.DataStore
import tetrago.polaris.app.data.DataStoreProvider
import tetrago.polaris.core.CoreModule

class CoreDataStore : DataStore(CoreModule.ID) {
    override val builder: BoxStoreBuilder = MyObjectBox.builder()
}
