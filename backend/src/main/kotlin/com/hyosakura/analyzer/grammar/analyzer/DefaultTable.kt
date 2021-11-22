package com.hyosakura.analyzer.grammar.analyzer

import com.hyosakura.analyzer.grammar.*

/**
 * @author LovesAsuna
 **/
class DefaultTable(private val grammar: Grammar) : LLTable {
    override val table: MutableMap<NonTerm, MutableMap<Term, MutableList<Symbol>?>> = mutableMapOf()
    override val head = grammar.head

    private val termList: List<Term> by lazy {
        grammar.rules.values.stream().flatMap {
            it.stream()
        }.flatMap {
            it.stream()
        }.filter {
            it is Term
        }.map {
            it as Term
        }.distinct().toList()
    }

    init {
        initTable()
    }

    private fun initTable() {
        grammar.rules.forEach { entry ->
            termList.forEach { term ->
                val rules = grammar.rules[entry.key]!!
                var correctRule: MutableList<Symbol>? = null
                rules.forEach {
                    val firstSymbol = it.first()
                    var follow: Set<Symbol>? = null
                    val first: Set<Symbol> = if (firstSymbol is Term) {
                        setOf(firstSymbol)
                    } else {
                        if (firstSymbol is Empty) {
                            setOf(Empty)
                        } else {
                            follow = grammar.follow[firstSymbol]!!
                            grammar.first[firstSymbol]!!
                        }
                    }
                    if (first.contains(term)) {
                        if (correctRule == null) {
                            correctRule = it
                        } else {
                            throw RuntimeException("Rule conflict!")
                        }
                    } else {
                        if (firstSymbol !is Term) {
                            if (firstSymbol == Empty ||
                                grammar.rules[firstSymbol]!!.stream().anyMatch { inner ->
                                    inner.contains(Empty)
                                }
                            ) {
                                if (correctRule == null) {
                                    if (follow != null && follow.contains(term)) {
                                        correctRule = it
                                    }
                                    if (grammar.follow[entry.key]!!.contains(term) && first.contains(Empty)) {
                                        correctRule = it
                                    }
                                }
                            }
                        }
                    }
                }
                table.computeIfAbsent(entry.key) { mutableMapOf() }[term] = correctRule
            }
        }
    }
}