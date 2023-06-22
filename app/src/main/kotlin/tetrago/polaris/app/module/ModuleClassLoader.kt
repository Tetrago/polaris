package tetrago.polaris.app.module

import okio.Path
import org.slf4j.LoggerFactory
import tetrago.polaris.module.ModuleProvider
import java.net.URLClassLoader

class ModuleClassLoader(
    file: Path,
    parent: ClassLoader
) : URLClassLoader(arrayOf(file.toFile().toURI().toURL()), parent) {
    companion object {
        private const val CLASSPATH = "tetrago.polaris.ksp.generated.Module"

        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    val moduleProvider: ModuleProvider?
        get() = try {
            logger.info("Loading module for file `{}`", urLs.single().file)

            val cls = loadClass(CLASSPATH)
            val getter = cls.getDeclaredMethod("get")

            getter.invoke(null) as ModuleProvider
        } catch(_: ClassNotFoundException) {
            null
        }

    private fun shallowLoadClass(name: String?): Class<*> {
        return super.loadClass(name)
    }

    override fun loadClass(name: String?): Class<*> {
        try {
            return super.loadClass(name)
        } catch(e: ClassNotFoundException) {
            if(name == CLASSPATH) throw e

            ModuleLoader.moduleMap.forEach { (module, loader) ->
                if(name?.startsWith(module.id) == true) {
                    return loader.shallowLoadClass(name)
                }
            }

            throw e
        }
    }
}
