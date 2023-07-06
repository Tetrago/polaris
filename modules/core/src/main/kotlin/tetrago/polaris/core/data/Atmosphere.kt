package tetrago.polaris.core.data

import io.objectbox.BoxStore
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.koin.core.annotation.Single
import tetrago.polaris.app.data.Registry
import tetrago.polaris.app.data.RegistryProvider
import tetrago.polaris.core.data.unit.Density
import tetrago.polaris.core.data.unit.DensityConverter
import tetrago.polaris.core.data.unit.toDensity

@Entity
data class Atmosphere(
    @Id
    var id: Long = 0,
    @Convert(converter = DensityConverter::class, dbType = Double::class)
    var density: Density = 0.toDensity()
) {
    var body: ToOne<Body> = ToOne(this, Atmosphere_.body)
    var gas: ToOne<Gas> = ToOne(this, Atmosphere_.gas)

    @JvmField
    @Transient
    @Suppress("PropertyName")
    var __boxStore: BoxStore? = null
}

@Single(binds = [RegistryProvider::class])
class AtmosphereRegistry : Registry<CoreDataStore, Atmosphere>(CoreDataStore::class, Atmosphere::class)
