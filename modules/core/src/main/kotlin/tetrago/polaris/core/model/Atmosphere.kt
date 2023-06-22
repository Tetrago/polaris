package tetrago.polaris.core.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import tetrago.polaris.core.model.unit.byDensity
import tetrago.polaris.core.model.unit.density

object Atmospheres : IntIdTable() {
    val body = reference("body", Bodies)
    val gas = reference("gas", Gases)
    val density = density("density")
}

class Atmosphere(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Atmosphere>(Atmospheres)

    var body by Body referencedOn Atmospheres.body
    var gas by Atmospheres.gas
    var density by Atmospheres.density.byDensity()
}
