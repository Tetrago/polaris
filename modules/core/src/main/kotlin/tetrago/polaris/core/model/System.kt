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
    val planets by Body referrersOn Bodies.system
}

fun System(name: String, block: System.() -> Unit): System {
    return System.new {
        this.name = name
        block()
    }
}

fun System.Body(name: String, block: Body.() -> Unit): Body {
    return Body.new {
        system = this@Body
        this.name = name
        block()
    }
}
