import NumberMultiplierOperation.*
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe

fun main() {
    fun part1(input: List<String>): ULong {
        return input.sumOf { it.multiplySomeNumbers() }
    }

    fun part2(input: List<String>): ULong {
        return input.joinToString(separator = "").doOrDoNotMultiplySomeNumbers()
    }

    val testInput1 = readInput("Day03_test1")
    withClue("part1") {
        part1(testInput1) shouldBe 161u
    }
    val testInput2 = readInput("Day03_test2")
    withClue("part2") {
        part2(testInput2) shouldBe 48u
    }

    val input = readInput("Day03")
    "Day 3".println()
    "  Part 1: ${part1(input)}".println()
    "  Part 2: ${part2(input)}".println()
}

private val mulRegex = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")
private val doRegex = Regex("do\\(\\)")
private val doNotRegex = Regex("don't\\(\\)")

private fun String.multiplySomeNumbers() = mulRegex.findAll(this)
    .sumOf { match -> match.groupValues.drop(1).map { it.toULong() }.reduce(ULong::times) }

private fun String.doOrDoNotMultiplySomeNumbers() = numberMultiplierOperations()
    .fold(NumberMultiplierState.initial(), NumberMultiplierState::transition).value

private fun String.numberMultiplierOperations(): Sequence<NumberMultiplierOperation> {
    val muls = mulRegex.findAll(this)
        .map { match -> Mul(match.range, match.groupValues.drop(1).map { it.toULong() }.reduce(ULong::times)) }

    val dos = doRegex.findAll(this)
        .map { Do(it.range) }

    val doNots = doNotRegex.findAll(this)
        .map { DoNot(it.range) }

    return (muls + dos + doNots).sortedBy { it.range.first }
}

private sealed interface NumberMultiplierOperation{

    val range: IntRange

    data class Mul(
        override val range: IntRange,
        val result: ULong,
    ) : NumberMultiplierOperation

    data class Do(
        override val range: IntRange,
    ) : NumberMultiplierOperation

    data class DoNot(
        override val range: IntRange,
    ) : NumberMultiplierOperation
}

private sealed interface NumberMultiplierState {

    val value: ULong

    fun transition(operation: NumberMultiplierOperation): NumberMultiplierState

    data class Doing(
        override val value: ULong
    ) : NumberMultiplierState {

        override fun transition(operation: NumberMultiplierOperation) = when(operation) {
            is Do -> this
            is DoNot -> NotDoing(value)
            is Mul -> copy(value = value + operation.result)
        }
    }

    data class NotDoing(
        override val value: ULong
    ) : NumberMultiplierState {

        override fun transition(operation: NumberMultiplierOperation) = when(operation) {
            is Do -> Doing(value)
            is DoNot, is Mul -> this
        }
    }

    companion object {
        fun initial(): NumberMultiplierState = Doing(value = 0u)
    }
}
