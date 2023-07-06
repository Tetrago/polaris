package tetrago.polaris.core.data

import io.objectbox.BoxStore
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne
import org.koin.core.annotation.Single
import tetrago.polaris.app.data.Registry
import tetrago.polaris.app.data.RegistryProvider
import tetrago.polaris.core.data.model.Orbit
import tetrago.polaris.core.data.model.OrbitConverter
import tetrago.polaris.core.data.unit.Distance
import tetrago.polaris.core.data.unit.DistanceConverter
import tetrago.polaris.core.data.unit.toDst

@Entity
data class Body(
    @Id
    var id: Long = 0,
    var name: String? = null,
    @Convert(converter = DistanceConverter::class, dbType = Double::class)
    var radius: Distance = 0.toDst(),
    @Convert(converter = OrbitConverter::class, dbType = String::class)
    var orbit: Orbit? = null
) {
    var system: ToOne<PlanetarySystem> = ToOne(this, Body_.system)
    var parent: ToOne<Body> = ToOne(this, Body_.parent)
    var type: ToOne<BodyType> = ToOne(this, Body_.type)
    @Backlink(to = "body")
    var deposits: ToMany<Deposit> = ToMany(this, Body_.deposits)
    @Backlink(to = "body")
    var atmospheres: ToMany<Atmosphere> = ToMany(this, Body_.atmospheres)

    @JvmField
    @Transient
    @Suppress("PropertyName")
    var __boxStore: BoxStore? = null
}

@Single(binds = [RegistryProvider::class])
class BodyRegistry : Registry<CoreDataStore, Body>(CoreDataStore::class, Body::class)
