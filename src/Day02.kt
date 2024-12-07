import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.oneOrMore
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.regexToken

fun main() {
    Day02.run()
}

private object Day02 : AoC<Int, Int>() {

    override val part1TestExpected = 2
    override val part2TestExpected = 4

    override fun part1(input: List<String>): Int {
        return input.map { ReportParser.parseToEnd(it) }.count { it.isSafe }
    }

    override fun part2(input: List<String>): Int {
        return input.map { ReportParser.parseToEnd(it) }
            .count { report -> report.isSafe || report.problemDampenedReports().any { it.isSafe } }
    }

}

private object ReportParser : Grammar<Report>() {
    private val digits by regexToken("\\d+")
    private val whitespace by regexToken("\\s+", ignore = true)

    private val level by digits use { text.toUInt() }

    override val rootParser by oneOrMore(level) map { Report(levels = it) }
}

private data class Report(private val levels: List<UInt>) {

    private val zippedWithNextLevels = levels.zipWithNext()
    private val isGraduallyIncreasing = zippedWithNextLevels.all { (a, b) -> a < b && (b - a) <= 3u }
    private val isGraduallyDecreasing = zippedWithNextLevels.all { (a, b) -> a > b && (a - b) <= 3u }

    val isSafe = isGraduallyIncreasing || isGraduallyDecreasing

    fun problemDampenedReports(): Sequence<Report> {
        return levels.indices.asSequence().map { index -> Report(levels.toMutableList().apply { removeAt(index) }) }
    }
}
