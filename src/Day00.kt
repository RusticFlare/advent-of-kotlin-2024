import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe

private const val DAY = "Day00"

fun main() {
    fun part1(input: List<String>): Number {
        return input.size
    }

//    fun part2(input: List<String>): Number {
//        return input.size
//    }

    val testInput = readInput("${DAY}_test")
    withClue("part1") {
        part1(testInput) shouldBe 0
    }
    withClue("part2") {
//        part2(testInput) shouldBe 0
    }

    val input = readInput(DAY)
    DAY.println()
    "  Part 1: ${part1(input)}".println()
//    "  Part 2: ${part2(input)}".println()
}
