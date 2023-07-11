package tetrago.polaris.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import okio.FileSystem
import org.pushingpixels.aurora.component.ScrollBarSizingConstants
import org.pushingpixels.aurora.component.model.Command
import org.pushingpixels.aurora.component.model.CommandButtonPresentationModel
import org.pushingpixels.aurora.component.model.LabelContentModel
import org.pushingpixels.aurora.component.model.SelectorContentModel
import org.pushingpixels.aurora.component.model.TextFieldStringContentModel
import org.pushingpixels.aurora.component.projection.CheckBoxProjection
import org.pushingpixels.aurora.component.projection.CommandButtonProjection
import org.pushingpixels.aurora.component.projection.HorizontalSeparatorProjection
import org.pushingpixels.aurora.component.projection.LabelProjection
import org.pushingpixels.aurora.component.projection.TextFieldStringProjection
import org.pushingpixels.aurora.theming.AuroraSkin
import org.pushingpixels.aurora.theming.AuroraSkinDefinition
import org.pushingpixels.aurora.theming.DecorationAreaType
import org.pushingpixels.aurora.theming.auroraBackground
import org.pushingpixels.aurora.window.AuroraApplicationScope
import org.pushingpixels.aurora.window.AuroraDecorationArea
import org.pushingpixels.aurora.window.AuroraWindow
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.save.SaveFile
import kotlin.math.absoluteValue
import kotlin.random.Random

@Composable
fun AuroraApplicationScope.NewSaveDialog(skin: AuroraSkinDefinition, onWindowClose: (Boolean) -> Unit) {
    val modules = remember { mutableStateMapOf(*ModuleLoader.modules.map { it to false }.toTypedArray()) }
    var name by remember { mutableStateOf("") }
    var seed by remember { mutableStateOf(Random.nextInt().absoluteValue.toString()) }
    var file by remember { mutableStateOf<SaveFile?>(null) }

    AuroraWindow(
        skin = skin,
        state = rememberWindowState(width = 300.dp, height = 400.dp),
        title = "New Save",
        onCloseRequest = { onWindowClose(false) }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AuroraDecorationArea(decorationAreaType = DecorationAreaType.None) {
                val state = rememberLazyListState()
                val colors = AuroraSkin.colors.getBackgroundColorScheme(
                    decorationAreaType = AuroraSkin.decorationAreaType
                )

                Row(modifier = Modifier.fillMaxWidth().auroraBackground().padding(top = 5.dp)) {
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

                Row(modifier = Modifier.fillMaxWidth().auroraBackground().padding(top = 5.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        LabelProjection(
                            contentModel = LabelContentModel("Seed")
                        ).project()
                    }

                    Column(modifier = Modifier.weight(3f)) {
                        TextFieldStringProjection(
                            contentModel = TextFieldStringContentModel(seed, onValueChange = {
                                seed = when {
                                    it.isEmpty() -> "0"
                                    it.toIntOrNull() == null -> seed
                                    it != "0" && it.startsWith("0") -> it.removePrefix("0")
                                    it.startsWith("-") -> it.removePrefix("-")
                                    else -> it
                                }
                            })
                        ).project(modifier = Modifier.fillMaxWidth())
                    }
                }

                HorizontalSeparatorProjection().project(modifier = Modifier.fillMaxWidth().padding(top = 5.dp))

                Row(modifier = Modifier.fillMaxWidth().weight(1f).auroraBackground()) {
                    LazyColumn(
                        modifier = Modifier.padding(end = ScrollBarSizingConstants.DefaultScrollBarThickness),
                        state = state
                    ) {
                        itemsIndexed(modules.toList()) { i, (module, enabled) ->
                            CheckBoxProjection(
                                contentModel = SelectorContentModel(module.name, selected = enabled, onClick = {
                                    modules[module] = !enabled
                                })
                            ).project(
                                modifier = Modifier.fillMaxWidth().height(32.dp)
                                    .background(if(i % 2 == 0) colors.backgroundFillColor else
                                        colors.accentedBackgroundFillColor)
                            )
                        }
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
                        contentModel = Command(
                            "Next",
                            isActionEnabled = name.isNotEmpty() && !name.matches(Regex(".*[\\\\\"].*")),
                            action = {
                                val enabledModules = modules.filter { it.value }.keys.toList()
                                file = SaveFile(name, enabledModules)
                            }
                        ),
                        presentationModel = CommandButtonPresentationModel(
                            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 5.dp)
                        )
                    ).project()
                }
            }
        }
    }

    SaveWriterDialog(skin, file, seed.toInt()) {
        if(it) {
            onWindowClose(true)
        } else {
            val oldFile = file
            file = null

            FileSystem.SYSTEM.deleteRecursively(oldFile!!.directory)
            onWindowClose(false)
        }
    }
}
