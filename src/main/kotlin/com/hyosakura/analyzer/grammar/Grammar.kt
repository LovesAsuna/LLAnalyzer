package com.hyosakura.analyzer.grammar

/**
 * @author LovesAsuna
 **/
interface Symbol {
    val symbol : String
}

class NonTerm(override val symbol: String) : Symbol

class Term(override val symbol: String) : Symbol

data class Rule(
    val head: NonTerm,
    val body: List<Symbol>
) {
    override fun toString(): String {
        val builder = StringBuilder()
        body.forEach {
            builder.append(it.symbol)
        }
        return "${head.symbol}->${builder}"
    }
}

class Grammar(
    val rules: MutableList<Rule>
) {
    override fun toString(): String {
        val builder = StringBuilder()
        rules.forEach {
            builder.append(it.toString())
            builder.append("\n")
        }
        return builder.toString()
    }
}