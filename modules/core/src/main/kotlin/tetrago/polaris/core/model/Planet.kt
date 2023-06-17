package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Planets : IntIdTable() {
    val system = reference("system", Systems)
    val parent = reference("parent", Planets).nullable()
    val name = varchar("name", 100).index()
    val radius = long("radius")
}

class Planet(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Planet>(Planets)

    var system by System referencedOn Planets.system
    var parent by Planet optionalReferencedOn Planets.parent
    var name by Planets.name
    var radius: Distance by Planets.radius.transform({ it.kilometers }, { it.toDistance() })
}
