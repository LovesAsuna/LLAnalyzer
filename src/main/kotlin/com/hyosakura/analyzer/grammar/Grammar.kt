package com.hyosakura.analyzer.grammar

/**
 * @author LovesAsuna
 **/
interface Symbol {
    val symbol: String
}

class NonTerm(override val symbol: String) : Symbol {
    override fun toString(): String = symbol
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NonTerm

        if (symbol != other.symbol) return false

        return true
    }

    override fun hashCode(): Int {
        return symbol.hashCode()
    }
}

class Term(override val symbol: String) : Symbol {
    override fun toString(): String = symbol

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Term

        if (symbol != other.symbol) return false

        return true
    }

    override fun hashCode(): Int {
        return symbol.hashCode()
    }
}

object Empty : Symbol {
    override val symbol: String = "ε"
    override fun toString(): String = symbol
}

object EOS : Symbol {
    override val symbol: String = "$"
    override fun toString(): String = symbol
}

class Grammar(
    val head: NonTerm,
    val rules: MutableMap<NonTerm, MutableList<MutableList<Symbol>>>,
) {
    val first: MutableMap<Symbol, MutableSet<Symbol>> = mutableMapOf()
    val follow: MutableMap<Symbol, MutableSet<Symbol>> = mutableMapOf()

    override fun toString(): String {
        val builder = StringBuilder()
        rules.forEach { entry ->
            builder.append("${entry.key}->")
            for (i in 0 until entry.value.size) {
                builder.append(entry.value[i].joinToString(""))
                if (i != entry.value.size - 1) {
                    builder.append("|")
                }
            }
            builder.append("\n")
        }
        return builder.toString()
    }

    fun getFirst(symbol: Symbol): MutableSet<Symbol> {
        val first = mutableSetOf<Symbol>()
        val rule = rules[symbol]!!
        for (i in 0 until rule.size) {
            val singleRule = rule[i]
            val singleFirst = mutableSetOf<Symbol>()
            var k = 0
            while (k <= singleRule.size) {
                val s = singleRule[k]
                val firstOfS = mutableSetOf<Symbol>()
                if (s !is NonTerm) {
                    singleFirst.add(s)
                    break
                } else {
                    firstOfS.addAll(getFirst(s))
                }
                singleFirst.addAll(firstOfS - Empty)
                if (!firstOfS.contains(Empty)) {
                    break
                }
                k++
            }
            if (k == singleRule.size + 1) {
                singleFirst.add(Empty)
            }
            first.addAll(singleFirst)
        }
        this.first.putIfAbsent(symbol, first)
        return first
    }
}