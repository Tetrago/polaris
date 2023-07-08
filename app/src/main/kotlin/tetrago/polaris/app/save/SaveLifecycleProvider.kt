package tetrago.polaris.app.save

import kotlin.random.Random

interface SaveLifecycleProvider {
    class AbortException : Exception()

    fun read(file: SaveFile)
    fun write(file: SaveFile, random: () -> Random)
}