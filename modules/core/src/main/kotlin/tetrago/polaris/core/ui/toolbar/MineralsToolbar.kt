package tetrago.polaris.core.ui.toolbar

import org.koin.core.annotation.Single
import tetrago.polaris.app.ui.toolbar.IconToolbar
import tetrago.polaris.app.ui.toolbar.ToolbarProvider
import tetrago.polaris.core.ui.window.InfrastructureWindow

@Single(binds = [ToolbarProvider::class])
class MineralsToolbar : IconToolbar<InfrastructureWindow>(
    "Minerals",
    InfrastructureWindow::class
)
