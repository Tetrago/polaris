package tetrago.polaris.core.ui.window

import org.koin.core.annotation.Single
import tetrago.polaris.app.ui.window.Window

@Single
class InfrastructureWindow : Window(TAG, "Infrastructure") {
    companion object {
        const val TAG = "tetrago.polaris.core.ui.window.InfrastructureWindow"
    }
}
