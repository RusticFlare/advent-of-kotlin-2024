import kotlin.io.path.Path
import kotlin.io.path.readText

abstract class AoC<A, B> {

    private val name = checkNotNull(this::class.simpleName) { "AoC class can't be anonymous" }

    protected abstract val part1TestExpected: A

    protected abstract val part2TestExpected: B

    protected open val part1TestSuffix = ""

    protected open val part2TestSuffix = ""

    protected abstract fun part1(input: List<String>): A

    protected abstract fun part2(input: List<String>): B

    fun run() {
        val testInput1 = readInput("${name}_test$part1TestSuffix")
        checkPart1(actual = part1(testInput1), expected = part1TestExpected)
        val testInput2 = readInput("${name}_test$part2TestSuffix")
        checkPart2(actual = part2(testInput2), expected = part2TestExpected)

        val input = readInput(name)
        name.println()
        "  Part 1: ${part1(input)}".println()
        "  Part 2: ${part2(input)}".println()
    }

    private fun checkPart1(actual: A, expected: A) {
        check(actual == expected) { "Part 1: Expected <$expected, actual <$actual>" }
    }

    private fun checkPart2(actual: B, expected: B) {
        check(actual == expected) { "Part 2: Expected <$expected, actual <$actual>" }
    }
}

/**
 * Reads lines from the given input txt file.
 */
private fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()
