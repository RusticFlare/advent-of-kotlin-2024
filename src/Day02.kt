import arrow.core.Nel
import arrow.core.toNonEmptyListOrNull
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.regexToken
import io.kotest.assertions.withClue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { ReportParser.parseToEnd(it) }.count { it.isSafe }
    }

//    fun part2(input: List<String>): Int {
//        return 0
//    }

    val testInput = readInput("Day02_test")
    withClue("part1") {
        part1(testInput) shouldBeExactly 2
    }
//    withClue("part2") {
//        part2(testInput) shouldBe 31u
//    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    "Day 2".println()
    "  Part 1: ${part1(input)}".println()
//    "  Part 2: ${part2(input)}".println()
}

private object ReportParser : Grammar<Report>() {
    private val digits by regexToken("\\d+")
    private val whitespace by regexToken("\\s+", ignore = true)

    private val level by digits use { text.toUInt() }

    override val rootParser by oneOrMore(level) map { Report(levels = it.toNonEmptyListOrNull()!!) }
}

data class Report(private val levels: Nel<UInt>) {

    private val zippedWithNextLevels = levels.zipWithNext()
    private val isGraduallyIncreasing = zippedWithNextLevels.all { (a, b) -> a < b && (a difference b) in 1u..3u }
    private val isGraduallyDecreasing = zippedWithNextLevels.all { (a, b) -> a > b && (a difference b) in 1u..3u }

    val isSafe = isGraduallyIncreasing || isGraduallyDecreasing
}
