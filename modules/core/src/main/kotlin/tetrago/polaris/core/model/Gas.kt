package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Gases : IntIdTable() {
    val name = varchar("name", 50)
    val symbol = varchar("symbol", 20)
}

class Gas(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Gas>(Gases)

    var name by Gases.name
    var symbol by Gases.symbol
}

fun Gas(name: String, symbol: String): Gas {
    return Gas.new {
        this.name = name
        this.symbol = symbol
    }
}
