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

        other as NonTerm

        if (symbol != other.symbol) return false

        return true
    }

    override fun hashCode(): Int {
        return symbol.hashCode()
    }
}

class Grammar(
    val head: NonTerm,
    val rules: MutableMap<NonTerm, MutableList<List<Symbol>>>
) {
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
}