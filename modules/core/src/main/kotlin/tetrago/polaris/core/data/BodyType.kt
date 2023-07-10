package tetrago.polaris.core.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import org.koin.core.annotation.Single
import tetrago.polaris.app.data.Registry
import tetrago.polaris.app.data.RegistryProvider

@Entity
data class BodyType(
    @Id
    var id: Long = 0,
    @Unique
    var name: String? = null,
    var solid: Boolean = false,
    var colonizable: Boolean = false
)

@Single(binds = [RegistryProvider::class])
class BodyTypeRegistry : Registry<CoreDataStore, BodyType>(CoreDataStore::class, BodyType_.name) {
    var star by hold("Star")
    var terrestrial by hold("Terrestrial")
    var gasGiant by hold("Gas Giant")
    var iceGiant by hold("Ice Giant")
    var asteroid by hold("Asteroid")
    var anchor by hold("Anchor")

    override fun create() {
        star = BodyType(solid = false, colonizable = false)
        terrestrial = BodyType(solid = true, colonizable = true)
        gasGiant = BodyType(solid = false, colonizable = false)
        iceGiant = BodyType(solid = false, colonizable = false)
        asteroid = BodyType(solid = true, colonizable = false)
        anchor = BodyType()
    }
}
