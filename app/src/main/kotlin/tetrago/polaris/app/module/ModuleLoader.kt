package tetrago.polaris.app.module

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.KoinApplication
import org.slf4j.LoggerFactory
import tetrago.polaris.app.config.Configuration
import tetrago.polaris.module.ModuleProvider

object ModuleLoader {
    private val logger = LoggerFactory.getLogger(this::class.java)

    val moduleMap = FileSystem.SYSTEM.list(Configuration.modulesPath)
        .filter { it.name.endsWith(".jar") }
        .mapNotNull { load(it) }
        .toMap()

    val modules get() = moduleMap.keys

    private fun load(file: Path): Pair<ModuleProvider, ModuleClassLoader>? {
        return ModuleClassLoader(file, javaClass.classLoader).let { loader ->
            loader.moduleProvider?.let {
                logger.info("Found module in file `{}`", file)
                it to loader
            }
        }
    }

    fun resolve(ids: List<String>): List<ModuleReference> {
        return ids.map { id ->
            ModuleReference(id, modules.find { it.id == id }).also {
                if(it.provider != null) {
                    logger.debug("Resolving module `{}` to `{}`", it.id, it.provider.name)
                } else {
                    logger.debug("Failed to resolve module `{}`", it.id)
                }
            }
        }
    }

    fun orderModules(list: List<ModuleProvider>): List<ModuleProvider> {
        val stack = mutableListOf<ModuleProvider>()
        val orders = list.toMutableList()

        while(orders.isNotEmpty()) {
            val swap = stack.toList()

            orders.removeAll { order ->
                val unresolved = order.dependencies.toMutableList().apply {
                    removeAll { dependency -> stack.any { it.id == dependency } }
                }

                if(unresolved.isEmpty()) {
                    stack.add(order)
                }

                unresolved.isEmpty()
            }

            if(swap == stack) {
                logger.error("Circular dependency between modules detected:")
                swap.forEach { logger.error("\t${it.id}") }
            }
        }

        return stack
    }

    inline fun <reified T : ModuleProvider> get(): ModuleProvider? {
        return moduleMap.mapNotNull { (key, _) -> key as? T }.singleOrNull()
    }
}
