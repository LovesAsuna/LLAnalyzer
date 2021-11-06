package com.hyosakura.analyzer.parser

import com.hyosakura.analyzer.grammar.Grammar
import com.hyosakura.analyzer.grammar.NonTerm
import com.hyosakura.analyzer.grammar.Rule
import com.hyosakura.analyzer.grammar.Term

/**
 * @author LovesAsuna
 **/
class GrammarScanner {
    private var counter1 = 0
    private var counter2 = 100

    fun parse(gramStr: String): Grammar {
        val lines = gramStr.lines().filterNot {
            it.isEmpty() || it.isBlank()
        }
        val ruleList = mutableListOf<Rule>()
        Grammar(
            mutableListOf<Rule>().apply {
                lines.forEach {
                    ruleList.add(parseSingleLine(it))
                }
            }
        )
        return Grammar(ruleList)
    }

    private fun parseSingleLine(gramStr: String): Rule {
        val splitStr = gramStr.split("->")
        if (splitStr.size != 2) throw RuntimeException("syntax error at $splitStr: $gramStr")
        val head = splitStr[0].trim()
        val body = splitStr[1].trim()
        return Rule(
            NonTerm(head),
            body.map {
                if (it.isLowerCase()) {
                    Term(it.toString())
                } else {
                    NonTerm(it.toString())
                }
            }
        )
    }
}