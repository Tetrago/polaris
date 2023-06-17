package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Systems : IntIdTable() {
    val name = varchar("name", 100)
}

class System(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<System>(Systems)

    var name by Systems.name
    val planets by Planet referrersOn Planets.system
}
