package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Specieses : IntIdTable() {
    val name = varchar("name", 100)
}

class Species(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Species>(Specieses)

    var name by Specieses.name
}