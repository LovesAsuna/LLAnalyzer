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
        val body = splitStr[1].trim().split("|")
        val ruleList = mutableListOf<MutableList<Symbol>>()
        body.forEach {
            if (it.isEmpty()) throw RuntimeException("syntax error at $gramStr")
            ruleList.add(
                it.trim().map { char ->
                    if (char == 'ε') {
                        Empty
                    } else if (char.isLowerCase()) {
                        Term(char.toString())
                    } else {
                        NonTerm(char.toString())
                    }
                }.toMutableList()
            )
        }
        return NonTerm(head) to ruleList
    }
}