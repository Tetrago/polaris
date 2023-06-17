package tetrago.polaris.core.ui.toolbar

import org.koin.core.annotation.Single
import tetrago.polaris.app.ui.toolbar.Toolbar
import tetrago.polaris.core.ui.window.InfrastructureWindow

@Single
class SystemToolbar : Toolbar("System", InfrastructureWindow.TAG, "system.png")