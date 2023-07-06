package tetrago.polaris.core.data

import io.objectbox.BoxStore
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import org.koin.core.annotation.Single
import tetrago.polaris.app.data.Registry
import tetrago.polaris.app.data.RegistryProvider
import tetrago.polaris.core.data.unit.Mass
import tetrago.polaris.core.data.unit.MassConverter
import tetrago.polaris.core.data.unit.toMass

@Entity
data class Deposit(
    @Id
    var id: Long = 0,
    @Convert(converter = MassConverter::class, dbType = Double::class)
    var mass: Mass = 0.toMass()
) {
    var body: ToOne<Body> = ToOne(this, Deposit_.body)
    var mineral: ToOne<Mineral> = ToOne(this, Deposit_.mineral)

    @JvmField
    @Transient
    @Suppress("PropertyName")
    var __boxStore: BoxStore? = null
}

@Single(binds = [RegistryProvider::class])
class DepositRegistry : Registry<CoreDataStore, Deposit>(CoreDataStore::class, Deposit::class)
