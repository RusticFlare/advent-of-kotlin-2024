import com.github.h0tk3y.betterParse.combinators.and
import com.github.h0tk3y.betterParse.combinators.map
import com.github.h0tk3y.betterParse.combinators.use
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.regexToken

fun main() {
    Day01.run()
}

private object Day01 : AoC<UInt, UInt>() {

    override val part1TestExpected = 11u
    override val part2TestExpected = 31u

    override fun part1(input: List<String>): UInt {
        val (leftIds, rightIds) = input.map { UIntPairParser.parseToEnd(it) }.unzip()

        return leftIds.sorted().zip(rightIds.sorted()) { l, r -> l difference r }.sum()
    }

    override fun part2(input: List<String>): UInt {
        val (leftIds, rightIds) = input.map { UIntPairParser.parseToEnd(it) }.unzip()

        return leftIds.sumOf { l -> l * rightIds.count { r -> l == r }.toUInt() }
    }

}

private object UIntPairParser : Grammar<Pair<UInt, UInt>>() {
    private val digits by regexToken("\\d+")
    private val whitespace by regexToken("\\s+", ignore = true)

    private val locationId by digits use { text.toUInt() }

    override val rootParser by (locationId and locationId) map { (l, r) -> l to r }
}
