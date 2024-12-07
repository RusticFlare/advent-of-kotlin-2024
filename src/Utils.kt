import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parseToEnd
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.max
import kotlin.math.min

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

infix fun UInt.difference(other: UInt): UInt {
    return max(this, other) - min(this, other)
}

fun <T> Grammar<T>.parseToEndOrNull(input: String): T? = runCatching { parseToEnd(input) }.getOrNull()
