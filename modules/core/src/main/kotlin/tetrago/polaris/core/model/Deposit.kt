package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import tetrago.polaris.core.model.unit.byMass
import tetrago.polaris.core.model.unit.mass

object Deposits : IntIdTable() {
    val body = reference("body", Bodies)
    val mineral = reference("mineral", Minerals)
    val mass = mass("mass")
}

class Deposit(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Deposit>(Deposits)

    var body by Body referencedOn Deposits.body
    var mineral by Mineral referencedOn Deposits.mineral
    var mass by Deposits.mass.byMass()
}
