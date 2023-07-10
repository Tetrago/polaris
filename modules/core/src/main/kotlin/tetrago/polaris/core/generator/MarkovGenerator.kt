package tetrago.polaris.core.generator

import okio.Source
import okio.buffer
import kotlin.random.Random

class MarkovGenerator(
    private val random: Random,
    dataset: List<String>
) {
    companion object {
        fun fromSource(random: Random, source: Source): MarkovGenerator {
            val lines = mutableListOf<String>()

            source.buffer().use {
                while(true) {
                    lines.add(it.readUtf8Line() ?: break)
                }
            }

            return MarkovGenerator(random, lines)
        }
    }

    private val firstCharacters: List<Char>
    private val map: Map<Char, List<Pair<Char, Float>>>

    init {
        class Counter {
            var value = 0
                private set

            fun inc() {
                value += 1
            }
        }

        val characters = mutableListOf<Char>()
        val sequences: MutableMap<Char, MutableMap<Char, Counter>> = mutableMapOf()

        dataset.forEach {
            var word = it
            characters.add(word[0])

            while(word.isNotEmpty()) {
                sequences
                    .getOrPut(word[0]) { mutableMapOf() }
                    .getOrPut(word.getOrElse(1) { '\u0000' }) { Counter() }
                    .inc()
                word = word.substring(1)
            }
        }

        val nodes = mutableMapOf<Char, MutableList<Pair<Char, Float>>>()

        sequences.forEach { (firstChar, connections) ->
            val tally = connections.map { it.value }.sumOf { it.value }

            nodes[firstChar] = mutableListOf<Pair<Char, Float>>().also { paths ->
                connections.forEach { (nextChar, count) ->
                    paths.add(nextChar to count.value.toFloat() / tally)
                }
            }
        }

        firstCharacters = characters.distinct()
        map = nodes
    }

    fun next(maxLength: Int = 256): String {
        var string = firstCharacters[random.nextInt(firstCharacters.size)].toString()

        outer@ while(true) {
            if(string.length >= maxLength) break

            val rand = random.nextFloat()
            var probability = 0f

            val list = map[string.last()] ?: break
            for((c, p) in list) {
                probability += p

                if(rand < probability) {
                    if(c == '\u0000') {
                        break@outer
                    }

                    string += c
                    continue@outer
                }
            }

            break
        }

        return string
    }
}