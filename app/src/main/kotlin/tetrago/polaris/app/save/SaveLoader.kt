package tetrago.polaris.app.save

import org.slf4j.LoggerFactory
import java.io.File
import kotlin.math.log

class SaveLoader(directory: File) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)

        fun parse(file: File): SaveFile? {
            if(!File(file.parentFile, "${file.nameWithoutExtension}.db").exists()) {
                logger.debug("Could not find database for save file `{}`", file)
                return null
            }

            logger.debug("Found save file `{}`", file)
            return SaveFile(file)
        }
    }

    val saveFiles = directory.walkTopDown()
        .filter { it.extension == "json" }
        .mapNotNull { parse(it) }
        .toList()
}