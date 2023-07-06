package tetrago.polaris.core.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import org.koin.core.annotation.Single
import tetrago.polaris.app.data.Registry
import tetrago.polaris.app.data.RegistryProvider

@Entity
data class Gas(
    @Id
    var id: Long = 0,
    @Unique
    var name: String? = null,
    @Unique
    var symbol: String? = null
)

@Single(binds = [RegistryProvider::class])
class GasRegistry : Registry<CoreDataStore, Gas>(CoreDataStore::class, Gas_.name) {
    var nitrogen by hold("Nitrogen")
    var oxygen by hold("Oxygen")
    var waterVapor by hold("Water Vapor")
    var carbonDioxide by hold("Carbon Dioxide")
    var argon by hold("Argon")

    override fun create() {
        nitrogen = Gas(symbol = "N2")
        oxygen = Gas(symbol = "O2")
        waterVapor = Gas(symbol = "H2O")
        carbonDioxide = Gas(symbol = "CO2")
        argon = Gas(symbol = "Ar2")
    }
}
