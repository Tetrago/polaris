package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Minerals : IntIdTable() {
    val name = varchar("name", 50)
}

class Mineral(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Mineral>(Minerals)

    var name by Minerals.name
}
