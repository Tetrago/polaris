package tetrago.polaris.app.save

import kotlin.random.Random

interface SaveLifecycleProvider {
    fun read(file: SaveFile)
    fun write(file: SaveFile, random: () -> Random)
}