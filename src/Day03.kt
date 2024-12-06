import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe

fun main() {
    fun part1(input: List<String>): ULong {
        return input.sumOf { it.multiplySomeNumbers() }
    }

//    fun part2(input: List<String>): Int {
//        return 0
//    }

    val testInput = readInput("Day03_test")
    withClue("part1") {
        part1(testInput) shouldBe 161u
    }
//    withClue("part2") {
//        part2(testInput) shouldBe 4
//    }

    val input = readInput("Day03")
    "Day 3".println()
    "  Part 1: ${part1(input)}".println()
//    "  Part 2: ${part2(input)}".println()
}

private val regex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

private fun String.multiplySomeNumbers() = regex.findAll(this)
    .map { match -> match.groupValues.drop(1).map { it.toULong() }.reduce(ULong::times) }
    .sum()

