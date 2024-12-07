import io.kotest.assertions.withClue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe

private const val DAY = "Day04"

private val XMAS = listOf('X', 'M', 'A', 'S')
private val SAMX = XMAS.asReversed()

fun main() {
    fun part1(input: List<String>): Int {
        val wordSearchCharacters = input.flatMapIndexed { y, line -> line.mapIndexed { x, character ->
            WordSearchCharacter(character = character, x = x, y = y)
        } }

        return wordSearchCharacters.run {
            xmasCount { x, _ -> x } + xmasCount { _, y -> y } + xmasCount { x, y -> x + y } + xmasCount { x, y -> x - y }
        }
    }

//    fun part2(input: List<String>): Number {
//        return input.size
//    }

    val testInput = readInput("${DAY}_test")
    withClue("part1") {
        part1(testInput) shouldBeExactly 18
    }
    withClue("part2") {
//        part2(testInput) shouldBe 0
    }

    val input = readInput(DAY)
    DAY.println()
    "  Part 1: ${part1(input)}".println()
//    "  Part 2: ${part2(input)}".println()
}

private data class WordSearchCharacter(
    val character: Char,
    val x: Int,
    val y: Int,
)

private fun List<WordSearchCharacter>.xmasCount(line: (x: Int, y: Int) -> Int) =
    groupBy(keySelector = { (_, x, y) -> line(x, y) }, valueTransform = { (c) -> c }).values.sumOf { it.xmasCount() }

private fun List<Char>.xmasCount() = windowed(size = 4, step = 1).count { it == XMAS || it == SAMX }
