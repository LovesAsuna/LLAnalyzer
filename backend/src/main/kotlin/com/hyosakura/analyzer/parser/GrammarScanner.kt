package com.hyosakura.analyzer.parser

import com.hyosakura.analyzer.grammar.*

/**
 * @author LovesAsuna
 **/
class GrammarScanner {
    fun parse(gramStr: String): Grammar {
        val lines = gramStr.lines().filterNot {
            it.isEmpty() || it.isBlank()
        }
        val ruleList = mutableMapOf<NonTerm, MutableList<MutableList<Symbol>>>()
        val headPair = parseSingleLine(lines.first())
        ruleList.computeIfAbsent(headPair.first) {
            mutableListOf()
        }.addAll(headPair.second)
        for (i in 1 until lines.size) {
            val pair = parseSingleLine(lines[i])
            ruleList.computeIfAbsent(pair.first) {
                mutableListOf()
            }.addAll(pair.second)
        }
        return Grammar(headPair.first, ruleList)
    }

    private fun parseSingleLine(gramStr: String): Pair<NonTerm, MutableList<MutableList<Symbol>>> {
        val splitStr = gramStr.split("->")
        if (splitStr.size != 2) throw RuntimeException("syntax error at $gramStr")
        val head = splitStr[0].trim()
        val body = splitStr[1].replace(" ", "").split("|")
        val ruleList = mutableListOf<MutableList<Symbol>>()
        body.forEach {
            if (it.isEmpty()) throw RuntimeException("syntax error at $gramStr")
            val singleRule = mutableListOf<Symbol>()
            var i = 0
            while (i < it.length) {
                val char = it[i]
                if (char == '\'') {
                    i++
                    continue
                }
                val symbol = if (char == '@') {
                    i++
                    Empty
                } else if (char.isUpperCase()) {
                    var count = 0
                    i++
                    while (i < it.length && it[i] == '\'') {
                        count++
                        i++
                    }
                    NonTerm("$char${"'".repeat(count)}")
                } else {
                    i++
                    Term("$char")
                }
                singleRule.add(symbol)
            }
            ruleList.add(singleRule)
        }
        return NonTerm(head) to ruleList
    }
}