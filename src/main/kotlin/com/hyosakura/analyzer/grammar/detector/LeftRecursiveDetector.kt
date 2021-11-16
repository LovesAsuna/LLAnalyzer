package com.hyosakura.analyzer.grammar.detector

import com.hyosakura.analyzer.grammar.Grammar
import com.hyosakura.analyzer.grammar.NonTerm
import com.hyosakura.analyzer.grammar.Symbol
import com.hyosakura.analyzer.grammar.Term

/**
 * @author LovesAsuna
 **/
class LeftRecursiveDetector : Detector {
    private lateinit var grammar: Grammar
    private lateinit var ruleList: List<NonTerm>

    override fun detect(grammar: Grammar): Grammar {
        this.grammar = grammar
        this.ruleList = grammar.rules.keys.toList()
        for (i in 0 until grammar.rules.size) {
            for (j in 0 until i) {
                val keyI = ruleList[i]
                val keyJ = ruleList[j]
                flatten(keyI, keyJ)
                eliminateRecursive(keyI)
            }
        }
        SimplifyDetector().detect(grammar)
        return grammar
    }

    private fun flatten(keyI: NonTerm, keyJ: NonTerm) {
        val newValue = mutableListOf<MutableList<Symbol>>()
        grammar.rules[keyI]!!.forEach {outer->
            if (outer.first() == keyJ) {
                val valueOfKeyJ = grammar.rules[keyJ]!!
                valueOfKeyJ.forEach {inner->
                    val newSingleValue = mutableListOf<Symbol>()
                    outer.forEach {
                        if (it != keyJ) {
                            newSingleValue.add(it)
                        } else {
                            newSingleValue.addAll(inner)
                        }
                    }
                    newValue.add(newSingleValue)
                }
            } else {
                newValue.add(outer)
            }
        }
        grammar.rules[keyI] = newValue
    }

    private fun eliminateRecursive(nonTerm: NonTerm) {
        val value = grammar.rules[nonTerm]!!
        val hasRecursive = value.stream().filter {
            it.first() == nonTerm
        }.toList()
        if (hasRecursive.isEmpty()) {
            return
        }
        val noRecursive = value - hasRecursive
        val newTerm = nonTerm.getTermWithComma(grammar)
        val newValue = mutableListOf<MutableList<Symbol>>()
        for (list in noRecursive) {
            newValue.add(list.also {
                it.add(newTerm)
            })
        }
        grammar.rules[nonTerm] = newValue
        val valueOfNewTerm = mutableListOf<MutableList<Symbol>>()
        for (list in hasRecursive) {
            valueOfNewTerm.add(list.also {
                it.removeFirst()
                it.add(newTerm)
            })
        }
        valueOfNewTerm.add(mutableListOf(Term("ε")))
        grammar.rules[newTerm] = valueOfNewTerm
    }
}