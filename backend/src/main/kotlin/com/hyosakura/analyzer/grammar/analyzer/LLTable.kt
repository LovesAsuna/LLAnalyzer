package com.hyosakura.analyzer.grammar.analyzer

import com.hyosakura.analyzer.grammar.NonTerm
import com.hyosakura.analyzer.grammar.Symbol
import com.hyosakura.analyzer.grammar.Term

/**
 * @author LovesAsuna
 **/
interface LLTable {
    val table: MutableMap<NonTerm, MutableMap<Term, MutableList<Symbol>?>>

    val head: NonTerm

    operator fun get(nonTerm: NonTerm): Map<Term, MutableList<Symbol>?> = table[nonTerm]!!
}