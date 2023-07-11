package tetrago.polaris.app.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import org.pushingpixels.aurora.component.AuroraBoxWithHighlights
import org.pushingpixels.aurora.component.ScrollBarSizingConstants
import org.pushingpixels.aurora.component.model.Command
import org.pushingpixels.aurora.component.model.CommandButtonPresentationModel
import org.pushingpixels.aurora.component.model.HorizontalAlignment
import org.pushingpixels.aurora.component.model.LabelContentModel
import org.pushingpixels.aurora.component.model.LabelPresentationModel
import org.pushingpixels.aurora.component.projection.CommandButtonProjection
import org.pushingpixels.aurora.component.projection.LabelProjection
import org.pushingpixels.aurora.component.projection.VerticalSeparatorProjection
import org.pushingpixels.aurora.theming.AuroraSkin
import org.pushingpixels.aurora.theming.AuroraSkinDefinition
import org.pushingpixels.aurora.theming.DecorationAreaType
import org.pushingpixels.aurora.theming.Side
import org.pushingpixels.aurora.theming.Sides
import org.pushingpixels.aurora.theming.auroraBackground
import org.pushingpixels.aurora.window.AuroraApplicationScope
import org.pushingpixels.aurora.window.AuroraDecorationArea
import org.pushingpixels.aurora.window.AuroraWindow
import tetrago.polaris.app.module.ModuleLoader
import tetrago.polaris.app.module.ModuleReference
import tetrago.polaris.app.save.SaveFile
import tetrago.polaris.app.save.SaveLoader

@Composable
fun AuroraApplicationScope.LauncherDialog(skin: AuroraSkinDefinition, onSaveLoaded: (SaveFile) -> Unit) {
    var newSave by remember { mutableStateOf(false) }
    var currentSave by remember { mutableStateOf<SaveFile?>(null) }
    var saves by remember { mutableStateOf(SaveLoader.saveFiles) }
    var modules by remember { mutableStateOf(emptyList<ModuleReference>()) }

    AuroraWindow(
        skin = skin,
        state = rememberWindowState(width = 500.dp, height = 400.dp),
        title = "Polaris",
        onCloseRequest = ::exitApplication
    ) {
        Column(modifier = Modifier.fillMaxSize().auroraBackground()) {
            AuroraDecorationArea(decorationAreaType = DecorationAreaType.Header) {
                Row(modifier = Modifier.auroraBackground().padding(5.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        LabelProjection(
                            contentModel = LabelContentModel("Saves")
                        ).project()
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        LabelProjection(
                            contentModel = LabelContentModel("Modules")
                        ).project()
                    }
                }
            }

            AuroraDecorationArea(decorationAreaType = DecorationAreaType.None) {
                Row(modifier = Modifier.weight(1f).auroraBackground()) {
                    SaveList(modifier = Modifier.weight(1f), saves, currentSave) {
                        currentSave = it
                        modules = ModuleLoader.resolve(it.description.modules)
                    }

                    VerticalSeparatorProjection().project(modifier = Modifier.fillMaxHeight())

                    ModuleList(modifier = Modifier.weight(1f), modules)
                }
            }

            AuroraDecorationArea(decorationAreaType = DecorationAreaType.Footer) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .auroraBackground()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CommandButtonProjection(
                        contentModel = Command("New", action = { newSave = true }),
                        presentationModel = CommandButtonPresentationModel(
                            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 5.dp)
                        )
                    ).project()

                    CommandButtonProjection(
                        contentModel = Command("Load", action = {

                        }),
                        presentationModel = CommandButtonPresentationModel(
                            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 5.dp)
                        )
                    ).project()
                }
            }
        }
    }

    if(newSave) {
        NewSaveDialog(skin) {
            newSave = false
            if(it) {
                saves = SaveLoader.saveFiles
            }
        }
    }
}

@Composable
fun SaveList(modifier: Modifier, saves: List<SaveFile>, currentSave: SaveFile?, onSaveFileChanged: (SaveFile) -> Unit) {
    val state = rememberLazyListState()
    val colors = AuroraSkin.colors.getBackgroundColorScheme(
        decorationAreaType = AuroraSkin.decorationAreaType
    )

    LazyColumn(modifier = modifier.padding(end = ScrollBarSizingConstants.DefaultScrollBarThickness), state = state) {
        itemsIndexed(saves) { i, it ->
            AuroraBoxWithHighlights(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(if(i % 2 == 0) colors.backgroundFillColor else colors.accentedBackgroundFillColor),
                selected = currentSave == it,
                onClick = { onSaveFileChanged(it) },
                sides = Sides(straightSides = Side.values().toSet())
            ) {
                LabelProjection(
                    contentModel = LabelContentModel(it.description.name),
                    presentationModel = LabelPresentationModel(inheritStateFromParent = true)
                ).project()
            }
        }
    }
}

@Composable
fun ModuleList(modifier: Modifier, modules: List<ModuleReference>) {
    val state = rememberLazyListState()
    val colors = AuroraSkin.colors.getBackgroundColorScheme(
        decorationAreaType = AuroraSkin.decorationAreaType
    )

    LazyColumn(modifier = modifier.padding(end = ScrollBarSizingConstants.DefaultScrollBarThickness), state = state) {
        itemsIndexed(modules) { i, it ->
            LabelProjection(
                contentModel = LabelContentModel(it.provider?.name ?: it.id)
            ).project(
                modifier = Modifier.height(32.dp)
                    .background(if(i % 2 == 0) colors.backgroundFillColor else colors.accentedBackgroundFillColor)
            )
        }
    }
}
