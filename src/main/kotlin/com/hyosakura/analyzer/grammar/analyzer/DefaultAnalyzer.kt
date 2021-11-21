package com.hyosakura.analyzer.grammar.analyzer

import com.hyosakura.analyzer.grammar.Empty
import com.hyosakura.analyzer.grammar.NonTerm
import com.hyosakura.analyzer.grammar.Symbol
import com.hyosakura.analyzer.grammar.Term
import java.util.*

/**
 * @author LovesAsuna
 **/
class DefaultAnalyzer(private val table: LLTable) : Analyzer {
    private val stack = LinkedList<Symbol>()

    override fun analyze(text: String): Boolean {
        stack.addLast(table.head)
        var point = 0
        while (stack.isNotEmpty()) {
            val currentSymbol = stack.removeLast()
            val currentChar = text[point]
            if (currentSymbol is NonTerm) {
                val list = table[currentSymbol][Term(currentChar.toString())] ?: return false
                while (list.isNotEmpty()) {
                    stack.addLast(list.removeLast())
                }
            } else {
                if (currentSymbol is Empty) {
                    continue
                }
                if (currentSymbol.symbol != currentChar.toString()) {
                    return false
                }
                point++
            }
        }
        return true
    }

}