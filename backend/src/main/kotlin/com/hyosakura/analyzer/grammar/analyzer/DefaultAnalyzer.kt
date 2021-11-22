package com.hyosakura.analyzer.grammar.analyzer

import com.hyosakura.analyzer.grammar.Empty
import com.hyosakura.analyzer.grammar.NonTerm
import com.hyosakura.analyzer.grammar.Symbol
import com.hyosakura.analyzer.grammar.Term
import java.util.*

/**
 * @author LovesAsuna
 **/
class DefaultAnalyzer(override val table: LLTable) : Analyzer {
    private val stack = LinkedList<Symbol>()
    override val result = Analyzer.DeriveResult()

    override fun analyze(text: String): Boolean {
        stack.addLast(table.head)
        var currentText = text
        var step = 1
        result.rows.add(Analyzer.DeriveResult.Row(step++, table.head.symbol, currentText))
        var point = 0
        while (stack.isNotEmpty()) {
            val currentSymbol = stack.removeLast()
            val currentChar = text[point]
            if (currentSymbol is NonTerm) {
                val list = table[currentSymbol][Term(currentChar.toString())] ?: return false
                result.rows.last().action = "$currentSymbol -> ${list.joinToString("")}"
                while (list.isNotEmpty()) {
                    stack.addLast(list.removeLast())
                }
                result.rows.add(Analyzer.DeriveResult.Row(step++, stack.joinToString(""), currentText))
            } else {
                if (currentSymbol is Empty) {
                    continue
                }
                if (currentSymbol.symbol != currentChar.toString()) {
                    return false
                }
                result.rows.last().action = "匹配"
                currentText = currentText.substring(1)
                result.rows.add(Analyzer.DeriveResult.Row(step++, stack.joinToString(""), currentText))
                point++
            }
        }
        result.rows.last().action = "成功"
        return true
    }
}

