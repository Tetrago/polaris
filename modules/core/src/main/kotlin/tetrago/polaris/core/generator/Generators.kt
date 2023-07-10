package tetrago.polaris.core.generator

import okio.source
import kotlin.random.Random

class Generators(private val random: Random) {
    private fun file(name: String) = MarkovGenerator.fromSource(
        random,
        this::class.java.getResourceAsStream("dataset/$name.txt")!!.source()
    )

    val body = file("body")
}