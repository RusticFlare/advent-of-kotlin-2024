private val XMAS = listOf('X', 'M', 'A', 'S')
private val SAMX = XMAS.asReversed()

fun main() {
    Day04.run()
}

private object Day04 : AoC<Int, Int>() {

    override val part1TestExpected = 18
    override val part2TestExpected = 9

    override fun part1(input: List<String>): Int {
        return wordSearchCharacters(input).run {
            xmasCount { x, _ -> x } + xmasCount { _, y -> y } + xmasCount { x, y -> x + y } + xmasCount { x, y -> x - y }
        }
    }

    override fun part2(input: List<String>): Int {
        return wordSearchCharacters(input).x_masCount()
    }
}

private fun wordSearchCharacters(input: List<String>): List<WordSearchCharacter> {
    return input.flatMapIndexed { y, line ->
        line.mapIndexed { x, character ->
            WordSearchCharacter(character = character, x = x, y = y)
        }
    }
}

private data class WordSearchCharacter(
    val character: Char,
    val x: Int,
    val y: Int,
)

private fun List<WordSearchCharacter>.xmasCount(line: (x: Int, y: Int) -> Int) =
    groupBy(keySelector = { (_, x, y) -> line(x, y) }, valueTransform = { (c) -> c }).values.sumOf { it.xmasCount() }

private fun List<Char>.xmasCount() = windowed(size = 4).count { it == XMAS || it == SAMX }

private fun List<WordSearchCharacter>.diagonalLines() =
    groupBy { (_, x, y) -> x + y }.values + groupBy { (_, x, y) -> x - y }.values

private fun List<WordSearchCharacter>.x_masCount() =
    diagonalLines().flatMap { it.windowed(size = 3) { (a, b, c) -> Triple(a, b, c) } }
        .filter { it.isMas() || it.isSam() }
        .groupingBy { (_, a, _) -> a }.eachCount()
        .count { (_, count) -> count >= 2 }

private fun Triple<WordSearchCharacter, WordSearchCharacter, WordSearchCharacter>.isMas() = let { (a, b, c) ->
    a.character == 'M' && b.character == 'A' && c.character == 'S'
}

private fun Triple<WordSearchCharacter, WordSearchCharacter, WordSearchCharacter>.isSam() = let { (a, b, c) ->
    a.character == 'S' && b.character == 'A' && c.character == 'M'
}
