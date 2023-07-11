package tetrago.polaris.app.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import org.pushingpixels.aurora.component.model.Command
import org.pushingpixels.aurora.component.model.CommandButtonPresentationModel
import org.pushingpixels.aurora.component.model.LabelContentModel
import org.pushingpixels.aurora.component.projection.CommandButtonProjection
import org.pushingpixels.aurora.component.projection.HorizontalSeparatorProjection
import org.pushingpixels.aurora.component.projection.LabelProjection
import org.pushingpixels.aurora.theming.AuroraSkinDefinition
import org.pushingpixels.aurora.theming.DecorationAreaType
import org.pushingpixels.aurora.theming.auroraBackground
import org.pushingpixels.aurora.window.AuroraApplicationScope
import org.pushingpixels.aurora.window.AuroraDecorationArea
import org.pushingpixels.aurora.window.AuroraWindow
import tetrago.polaris.app.data.DataStoreProvider
import tetrago.polaris.app.data.RegistryProvider
import tetrago.polaris.app.koin.ModuleKoinContext
import tetrago.polaris.app.koin.getModuleKoin
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.save.SaveDataWriter
import tetrago.polaris.app.save.SaveFile
import tetrago.polaris.app.save.SaveLifecycleProvider
import tetrago.polaris.module.ModuleProvider
import kotlin.random.Random

@Composable
fun AuroraApplicationScope.SaveWriterDialog(skin: AuroraSkinDefinition, file: SaveFile?, seed: Int, onWindowClose: (Boolean) -> Unit) {
    DisposableEffect(file) {
        if(file != null) {
            ModuleKoinContext.push()

            getModuleKoin().apply {
                loadModules(file.modules.map { it.modules }.flatten())

                getAll<DataStoreProvider>().forEach { it.load(file.directory) }
                getAll<RegistryProvider>().forEach { it.create() }
            }
        }

        onDispose {
            if(file != null) {
                getModuleKoin().apply {
                    getAll<SaveDataWriter>().forEach { it.initialize(Random(seed)) }

                    val queue = getAll<SaveLifecycleProvider>()
                        .groupBy { it.javaClass.classLoader }
                        .map { it.value.iterator() }
                        .toMutableList()

                    while(queue.isNotEmpty()) {
                        queue.forEach { it.next().write(file) { Random(seed) } }
                        queue.removeIf { !it.hasNext() }
                    }
                }

                ModuleKoinContext.pop()
            }
        }
    }

    if(file != null) {
        AuroraWindow(
            skin = skin,
            state = rememberWindowState(width = 300.dp, height = 500.dp),
            title = "New Save",
            onCloseRequest = { onWindowClose(false) }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AuroraDecorationArea(decorationAreaType = DecorationAreaType.None) {
                    Row(modifier = Modifier.fillMaxWidth().weight(1f).auroraBackground()) {
                        for(module in file.modules) {
                            DisplayModule(module)
                        }
                    }
                }

                AuroraDecorationArea(decorationAreaType = DecorationAreaType.Footer) {
                    Row(
                        modifier = Modifier.fillMaxWidth().auroraBackground().padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CommandButtonProjection(
                            contentModel = Command("Cancel", action = { onWindowClose(false) }),
                            presentationModel = CommandButtonPresentationModel(
                                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 5.dp)
                            )
                        ).project()

                        CommandButtonProjection(
                            contentModel = Command("Next", action = { onWindowClose(true) }),
                            presentationModel = CommandButtonPresentationModel(
                                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 5.dp)
                            )
                        ).project()
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayModule(module: ModuleProvider) {
    LabelProjection(
        contentModel = LabelContentModel(module.name)
    ).project(modifier = Modifier.fillMaxWidth())

    HorizontalSeparatorProjection().project(modifier = Modifier.fillMaxWidth().height(5.dp))

    val writers = getModuleKoin().getAll<SaveDataWriter>()
        .filter { it.javaClass.classLoader == ModuleLoader.moduleMap[module] }

    for(writer in writers) {
        writer.display()
    }

    Spacer(modifier = Modifier.fillMaxWidth().height(32.dp))
}
