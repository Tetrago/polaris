package tetrago.polaris.app

import okio.Path.Companion.toOkioPath
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.api.io.TempDir
import org.koin.dsl.module
import org.koin.dsl.onClose
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import tetrago.polaris.app.data.RegistryProvider
import tetrago.polaris.core.data.BodyRegistry
import tetrago.polaris.core.data.Body_
import tetrago.polaris.core.data.CoreDataStore
import tetrago.polaris.core.data.DataModule
import tetrago.polaris.core.data.PlanetarySystemRegistry
import tetrago.polaris.core.data.PlanetarySystem_
import tetrago.polaris.sol.save.Writer
import java.io.File
import kotlin.random.Random
import kotlin.test.assertNotNull

class SystemTest : KoinTest {
    companion object {
        @TempDir
        lateinit var tempDir: File
    }

    private val planetarySystemRegistry: PlanetarySystemRegistry by inject()
    private val bodyRegistry: BodyRegistry by inject()

    @JvmField
    @RegisterExtension
    val extension = KoinTestExtension.create {
        modules(
            DataModule().module,
            module {
                single { CoreDataStore() } onClose { it?.close() }
                single { Writer() }
            }
        )
    }

    @Test
    fun dataTest() {
        get<CoreDataStore>().load(tempDir.toOkioPath())
        getKoin().getAll<RegistryProvider>().forEach { it.create() }
        get<Writer>().initialize(Random(0))

        assertThat(planetarySystemRegistry.store.all.size, `is`(1))
        assertThat(bodyRegistry.store.all.size, `is`(10))

        val sol = planetarySystemRegistry.store.query(PlanetarySystem_.name.equal("Sol")).build().use { it.findFirst() }

        assertNotNull(sol)
        assertThat(sol.bodies.size, `is`(10))

        val sun = bodyRegistry.store.query(Body_.name.equal("Sun")).build().use { it.findFirst() }

        assertNotNull(sun)

        val earth = bodyRegistry.store.query(Body_.name.equal("Earth")).build().use { it.findFirst() }

        assertNotNull(earth)
        assertThat(earth.system.target, `is`(sol))
        assertThat(earth.parent.target, `is`(sun))
    }
}
