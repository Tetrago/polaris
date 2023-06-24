package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import tetrago.polaris.core.model.data.Orbit
import tetrago.polaris.core.model.data.byOrbit
import tetrago.polaris.core.model.data.orbit
import tetrago.polaris.core.model.unit.Distance
import tetrago.polaris.core.model.unit.byDistance
import tetrago.polaris.core.model.unit.distance

object Bodies : IntIdTable() {
    val system = reference("system", Systems)
    val parent = reference("parent", Bodies).nullable()
    val name = varchar("name", 100)
    val radius = distance("radius")
    val orbit = orbit("orbit")
    val type = reference("type", BodyTypes)
}

class Body(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Body>(Bodies)

    var system by System referencedOn Bodies.system
    var parent by Body optionalReferencedOn Bodies.parent
    var name by Bodies.name
    var radius: Distance by Bodies.radius.byDistance()
    var orbit: Orbit by Bodies.orbit.byOrbit()
    var type by BodyType referencedOn Bodies.type
    val deposits by Deposit referrersOn Deposits.body
    val atmospheres by Atmosphere referrersOn Atmospheres.body
}

fun Body.Body(name: String, block: Body.() -> Unit): Body {
    return Body.new {
        system = this@Body.system
        parent = this@Body
        this.name = name
        block()
    }
}
