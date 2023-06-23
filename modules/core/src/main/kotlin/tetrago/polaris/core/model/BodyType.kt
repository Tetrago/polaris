package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object BodyTypes : IntIdTable() {
    val name = varchar("name", 20)
    val solid = bool("solid")
    val colonizable = bool("colonizable")
}

class BodyType(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<BodyType>(BodyTypes)

    var name by BodyTypes.name
    var solid by BodyTypes.solid
    var colonizable by BodyTypes.colonizable
}

fun BodyType(name: String, solid: Boolean, colonizable: Boolean): BodyType {
    return BodyType.new {
        this.name = name
        this.solid = solid
        this.colonizable = colonizable
    }
}
