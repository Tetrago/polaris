package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Bodies : IntIdTable() {
    val system = reference("system", Systems)
    val parent = reference("parent", Bodies).nullable()
    val name = varchar("name", 100)
    val radius = long("radius")
    val orbit = text("orbit")
}

class Body(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Body>(Bodies)

    var system by System referencedOn Bodies.system
    var parent by Body optionalReferencedOn Bodies.parent
    var name by Bodies.name
    var radius: Distance by Bodies.radius.byDistance()
    var orbit: Orbit by Bodies.orbit.byOrbit()
}
