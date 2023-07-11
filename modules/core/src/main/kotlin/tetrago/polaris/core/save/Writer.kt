package tetrago.polaris.core.save

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.koin.core.annotation.Single
import org.koin.core.component.inject
import org.koin.dsl.bind
import org.koin.dsl.module
import org.pushingpixels.aurora.component.model.LabelContentModel
import org.pushingpixels.aurora.component.model.TextFieldStringContentModel
import org.pushingpixels.aurora.component.projection.LabelProjection
import org.pushingpixels.aurora.component.projection.TextFieldStringProjection
import tetrago.polaris.app.koin.ModuleKoinComponent
import tetrago.polaris.app.koin.getModuleKoin
import tetrago.polaris.app.save.SaveDataWriter
import tetrago.polaris.core.data.PlanetarySystemRegistry
import tetrago.polaris.core.generator.Generators
import tetrago.polaris.core.generator.PlanetarySystemGenerator
import tetrago.polaris.core.generator.PlanetarySystemProvider
import kotlin.random.Random

@Single
class Writer : SaveDataWriter, ModuleKoinComponent {
    private val planetarySystemProvider: PlanetarySystemProvider by inject()
    private val planetarySystemRegistry: PlanetarySystemRegistry by inject()

    private val name = mutableStateOf("Human")

    @Composable
    override fun display() {
        var name by remember { name }

        Row(modifier = Modifier.fillMaxWidth()) {
            LabelProjection(
                contentModel = LabelContentModel("Species")
            ).project()
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                LabelProjection(
                    contentModel = LabelContentModel("Name")
                ).project()
            }

            Column(modifier = Modifier.weight(3f)) {
                TextFieldStringProjection(
                    contentModel = TextFieldStringContentModel(name, onValueChange = { name = it })
                ).project(modifier = Modifier.fillMaxWidth())
            }
        }
    }

    override fun initialize(random: Random) {
        getModuleKoin().loadModules(listOf(module {
            single { Generators(random) }
            single { PlanetarySystemGenerator(random) } bind PlanetarySystemProvider::class
        }))

        planetarySystemRegistry.store.put(planetarySystemProvider.generate())
    }
}
