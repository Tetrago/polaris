package tetrago.polaris.app.koin

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication

object ModuleKoinContext {
    private val stack = mutableListOf<KoinApplication>()

    fun push() = push(koinApplication())
    fun push(koin: KoinApplication) = stack.add(koin)

    fun pop() = stack.lastOrNull()?.let {
        it.close()
        stack.removeLast()
    }

    val koin: Koin get() = stack.last().koin
}

fun getModuleKoin() = ModuleKoinContext.koin
