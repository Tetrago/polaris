package tetrago.polaris.core.data

import io.objectbox.BoxStore
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToMany
import org.koin.core.annotation.Single
import tetrago.polaris.app.data.Registry
import tetrago.polaris.app.data.RegistryProvider

@Entity
data class PlanetarySystem(
    @Id
    var id: Long = 0,
    @Unique
    var name: String? = null
) {
    @Backlink(to = "system")
    var bodies: ToMany<Body> = ToMany(this, PlanetarySystem_.bodies)

    @JvmField
    @Transient
    @Suppress("PropertyName")
    var __boxStore: BoxStore? = null
}

@Single(binds = [RegistryProvider::class])
class PlanetarySystemRegistry : Registry<CoreDataStore, PlanetarySystem>(CoreDataStore::class, PlanetarySystem::class)
