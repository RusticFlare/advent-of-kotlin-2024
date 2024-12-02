import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.regexToken
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): UInt {
        val (leftIds, rightIds) = input.map { UIntPairParser.parseToEnd(it) }.unzip()

        return leftIds.sorted().zip(rightIds.sorted()) { l, r -> l difference r }.sum()
    }

    fun part2(input: List<String>): UInt {
        val (leftIds, rightIds) = input.map { UIntPairParser.parseToEnd(it) }.unzip()

        return leftIds.sumOf { l -> l * rightIds.count { r -> l == r }.toUInt() }
    }

    val testInput = readInput("Day01_test")
    withClue("part1") {
        part1(testInput) shouldBe 11u
    }
    withClue("part2") {
        part2(testInput) shouldBe 31u
    }

    val input = readInput("Day01")
    "Day 1".println()
    "  Part 1: ${part1(input)}".println()
    "  Part 2: ${part2(input)}".println()
}

private object UIntPairParser : Grammar<Pair<UInt, UInt>>() {
    private val digits by regexToken("\\d+")
    private val whitespace by regexToken("\\s+", ignore = true)

    private val locationId by digits use { text.toUInt() }

    override val rootParser by (locationId and locationId) map  { (l, r) -> l to r }
}
