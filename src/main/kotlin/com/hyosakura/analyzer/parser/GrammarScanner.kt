package com.hyosakura.analyzer.parser

import com.hyosakura.analyzer.grammar.Grammar
import com.hyosakura.analyzer.grammar.NonTerm
import com.hyosakura.analyzer.grammar.Symbol
import com.hyosakura.analyzer.grammar.Term

/**
 * @author LovesAsuna
 **/
class GrammarScanner {
    fun parse(gramStr: String): Grammar {
        val lines = gramStr.lines().filterNot {
            it.isEmpty() || it.isBlank()
        }
        val ruleList = mutableMapOf<NonTerm, MutableList<List<Symbol>>>()
        val headPair = parseSingleLine(lines.first())
        ruleList.computeIfAbsent(headPair.first) {
            mutableListOf()
        }.add(headPair.second)
        for (i in 1 until lines.size) {
            val pair = parseSingleLine(lines[i])
            ruleList.computeIfAbsent(pair.first) {
                mutableListOf()
            }.add(pair.second)
        }
        return Grammar(headPair.first, ruleList)
    }

    private fun parseSingleLine(gramStr: String): Pair<NonTerm, List<Symbol>> {
        val splitStr = gramStr.split("->")
        if (splitStr.size != 2) throw RuntimeException("syntax error at $splitStr: $gramStr")
        val head = splitStr[0].trim()
        val body = splitStr[1].trim()
        return NonTerm(head) to body.map {
            if (it.isLowerCase()) {
                Term(it.toString())
            } else {
                NonTerm(it.toString())
            }
        }
    }
}