package tetrago.polaris.core.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import org.koin.core.annotation.Single
import tetrago.polaris.app.data.Registry
import tetrago.polaris.app.data.RegistryProvider

@Entity
data class Species(
    @Id
    var id: Long = 0,
    @Unique
    var name: String? = null
)

@Single(binds = [RegistryProvider::class])
class SpeciesRegistry : Registry<CoreDataStore, Species>(CoreDataStore::class, Species::class)
