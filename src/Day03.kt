import NumberMultiplierOperation.*

fun main() {
    Day03.run()
}

private object Day03 : AoC<ULong, ULong>() {

    override val part1TestExpected: ULong = 161u
    override val part2TestExpected: ULong = 48u

    override val part1TestSuffix = "1"
    override val part2TestSuffix = "2"

    override fun part1(input: List<String>): ULong {
        return input.sumOf { it.multiplySomeNumbers() }
    }

    override fun part2(input: List<String>): ULong {
        return input.joinToString(separator = "").doOrDoNotMultiplySomeNumbers()
    }

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

private sealed interface NumberMultiplierOperation {

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

        override fun transition(operation: NumberMultiplierOperation) = when (operation) {
            is Do -> this
            is DoNot -> NotDoing(value)
            is Mul -> copy(value = value + operation.result)
        }
    }

    data class NotDoing(
        override val value: ULong
    ) : NumberMultiplierState {

        override fun transition(operation: NumberMultiplierOperation) = when (operation) {
            is Do -> Doing(value)
            is DoNot, is Mul -> this
        }
    }

    companion object {
        fun initial(): NumberMultiplierState = Doing(value = 0u)
    }
}
