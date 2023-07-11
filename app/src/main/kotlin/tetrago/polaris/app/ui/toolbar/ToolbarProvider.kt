package tetrago.polaris.app.ui.toolbar

import org.pushingpixels.aurora.component.projection.CommandButtonProjection

interface ToolbarProvider {
    fun build(): CommandButtonProjection
}
