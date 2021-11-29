package ca.josue.rational

import java.math.BigInteger

class Rational (private val numerator: BigInteger, private val denominator: BigInteger) : Comparable<Rational>{

    init {
        if (denominator == BigInteger.ZERO)
            throw IllegalArgumentException("Denominator cannot be 0")
    }

    // Redefinition des opérateurs
    operator fun plus(other: Rational): Rational {
        val num = (numerator * other.denominator) + (denominator * other.numerator)
        val den = denominator * other.denominator
        return num.divBy(den)
    }

    operator fun minus(other: Rational) : Rational{
        val num = (numerator * other.denominator) - (denominator * other.numerator)
        val den = denominator * other.denominator
        return num.divBy(den)
    }

    operator fun times(other: Rational): Rational{
        return (numerator * other.numerator).divBy(denominator * other.denominator)
    }

    operator fun div(other: Rational):Rational{
        return (numerator * other.denominator).divBy(denominator * other.numerator)
    }

    operator fun unaryMinus(): Rational = Rational(numerator.negate(),denominator)

    override fun compareTo(other: Rational): Int {
        return (numerator * other.denominator).compareTo(denominator * other.numerator)
    }

    private fun simplify(rational: Rational): Rational{
        val greatCommonDivisor = rational.numerator.gcd(rational.denominator)
        val num = rational.numerator / greatCommonDivisor
        val den = rational.denominator / greatCommonDivisor
        return Rational(num, den)
    }

    private fun formatRational(): String = "$numerator/$denominator"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as Rational
        val thisSimplified = simplify(this)
        val otherSimplified = simplify(other)
        val thisAsDouble = thisSimplified.numerator.toDouble() / thisSimplified.denominator.toDouble()
        val otherAsDouble = otherSimplified.numerator.toDouble() / otherSimplified.denominator.toDouble()
        return thisAsDouble == otherAsDouble
    }

    override fun toString(): String {
        val shouldBeOneNumber = denominator == BigInteger.ONE || numerator % denominator == BigInteger.ZERO
        return when {
            shouldBeOneNumber -> (numerator / denominator).toString()
            else -> {
                val thisSimplified = simplify(this)

                if (thisSimplified.denominator < BigInteger.ZERO || (thisSimplified.numerator < BigInteger.ZERO && thisSimplified.denominator < BigInteger.ZERO)){
                    Rational(thisSimplified.numerator.negate(), thisSimplified.denominator.negate()).formatRational()
                }
                else{
                    Rational(thisSimplified.numerator, thisSimplified.denominator).formatRational()
                }
            }
        }
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }
}

fun String.toRational():Rational{
    val ratio = split('/')
    return when (ratio.size) {
        1 -> Rational(ratio[0].toBigInteger(), BigInteger.ONE)
        2 -> Rational(ratio[0].toBigInteger(), ratio[1].toBigInteger())
        else -> throw IllegalArgumentException("Invalid format")
    }
}

// Infix
infix fun Int.divBy(other: Int):Rational = Rational(toBigInteger(), other.toBigInteger())
infix fun Long.divBy(other: Long): Rational = Rational(toBigInteger(), other.toBigInteger())
infix fun BigInteger.divBy(other: BigInteger) = Rational(this, other)



fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}