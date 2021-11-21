package com.hyosakura.analyzer.grammar.detector

import com.hyosakura.analyzer.grammar.*

/**
 * @author LovesAsuna
 **/
class LeftCommonFactorDetector : Detector {
    override fun detect(grammar: Grammar): Grammar {
        val iterator = grammar.rules.iterator()
        val map = mutableMapOf<NonTerm, MutableList<MutableList<Symbol>>>()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key = entry.key
            val value = entry.value
            val firstTerm = value.first().first()
            if (firstTerm is Term) {
                if (
                    value.size > 1 && value.stream().allMatch {
                        it.first() == firstTerm
                    }
                ) {
                    val newValue = mutableListOf<MutableList<Symbol>>()
                    val newTerm = key.getTermWithComma(grammar)
                    newValue.add(mutableListOf(firstTerm, newTerm))
                    value.forEach {
                        it.removeFirst()
                        if (it.isEmpty()) {
                            it.add(Empty)
                        }
                    }
                    map[newTerm] = value
                    entry.setValue(newValue)
                }
            }
        }
        map.forEach {
            grammar.rules[it.key] = it.value
        }
        return grammar
    }
}