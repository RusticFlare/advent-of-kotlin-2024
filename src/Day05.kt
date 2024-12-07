import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken

fun main() {
    Day05.run()
}

private object Day05 : AoC<Int, Int>() {

    override val part1TestExpected = 143
    override val part2TestExpected = 0

    override fun part1(input: List<String>): Int {
        val pageOrderingRules = input
            .mapNotNull { runCatching { PageOrderingRuleParser.parseToEnd(it) }.getOrNull() }
            .comparator()

        return input
            .mapNotNull { runCatching { PagesToProduceInEachUpdateParser.parseToEnd(it) }.getOrNull() }
            .filter { it.isSortedWith(pageOrderingRules) }
            .sumOf { it[it.size / 2].value }
    }

    override fun part2(input: List<String>): Int {
        return 0
    }
}

private object PageOrderingRuleParser : Grammar<Pair<PageNumber, PageNumber>>() {

    private val digits by regexToken("\\d+")
    private val pipe by literalToken("|")

    private val pageNumber by digits use { text.toPageNumber() }

    override val rootParser by (pageNumber and -pipe and pageNumber) map { (x, y) -> x to y }
}

private object PagesToProduceInEachUpdateParser : Grammar<List<PageNumber>>() {

    private val digits by regexToken("\\d+")
    private val comma by literalToken(",")

    private val pageNumber by digits use { text.toPageNumber() }

    override val rootParser by separatedTerms(term = pageNumber, separator = comma)
}

private fun List<Pair<PageNumber, PageNumber>>.comparator() = object : Comparator<PageNumber> {

    private val beforeToAfter = groupBy(keySelector = { (a, _) -> a }, valueTransform = { (_, b) -> b })
    private val afterToBefore = groupBy(keySelector = { (_, b) -> b }, valueTransform = { (a, _) -> a })

    override fun compare(o1: PageNumber, o2: PageNumber) = when {
        beforeToAfter[o1]?.contains(o2) == true -> -1
        afterToBefore[o1]?.contains(o2) == true -> 1
        else -> 0
    }
}

@JvmInline
private value class PageNumber(val value: Int)

private fun String.toPageNumber() = PageNumber(toInt())

private fun <T> List<T>.isSortedWith(comparator: Comparator<T>) = this == this.sortedWith(comparator)
