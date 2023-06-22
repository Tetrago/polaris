package tetrago.polaris.core

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.slf4j.LoggerFactory
import tetrago.polaris.core.model.Body
import tetrago.polaris.core.model.Bodies
import tetrago.polaris.core.model.System
import tetrago.polaris.core.model.Systems
import tetrago.polaris.core.model.unit.toDst
import java.io.File
import java.sql.Connection

class ModelTest {
    companion object {
        @TempDir
        lateinit var tempDir: File

        private val logger = LoggerFactory.getLogger(ModelTest::class.java)
    }

    @Test
    fun databaseTest() {
        Database.connect("jdbc:sqlite:${File(tempDir, "test.db")}", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

        logger.info("Created database")

        transaction {
            SchemaUtils.create(Bodies, Systems)

            logger.info("Creating system...")
            val sol = System.new { name = "Sol" }

            logger.info("Creating parent planet...")
            val sun = Body.new {
                system = sol
                name = "Sun"
                radius = 696340.toDst()
            }

            logger.info("Creating child planet...")
            Body.new {
                system = sol
                parent = sun
                name = "Mercury"
                radius = 2440.toDst()
            }
        }

        logger.info("Saved database")

        transaction {
            logger.info("Loaded database")

            assertThat(System.count(), `is`(1))
            System.all().single().also { system ->
                assertThat(system.name, `is`("Sol"))
                assertThat(system.planets.count(), `is`(2))

                val it = system.planets.iterator()

                val sol = it.next().also {
                    assertThat(it.parent, nullValue())
                    assertThat(it.name, `is`("Sun"))
                }

                it.next().also {
                    assertThat(it.parent, `is`(sol))
                    assertThat(it.name, `is`("Mercury"))
                }
            }
        }
    }
}
