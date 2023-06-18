package tetrago.polaris.app.module

import org.slf4j.LoggerFactory
import tetrago.polaris.module.ModuleProvider
import java.io.File
import java.net.URLClassLoader

class ModuleLoader(directory: File) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)

        fun load(file: File): ModuleProvider? {
            return try {
                val loader = URLClassLoader(arrayOf(file.toURI().toURL()), ModuleLoader::class.java.classLoader)
                val module = Class.forName("tetrago.polaris.ksp.generated.Module", true, loader)
                val method = module.getDeclaredMethod("get")

                logger.info("Found module in file `{}`", file)
                method.invoke(null) as ModuleProvider
            } catch(e: ClassNotFoundException) {
                logger.info("Found no module in file `{}`", file)
                null
            }
        }
    }

    val modules = directory.walkTopDown()
        .filter { it.extension == "jar" }
        .mapNotNull { load(it) }
        .toList()

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
}
