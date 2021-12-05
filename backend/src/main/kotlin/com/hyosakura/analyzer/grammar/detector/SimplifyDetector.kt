package com.hyosakura.analyzer.grammar.detector

import com.hyosakura.analyzer.grammar.*
import java.util.*

/**
 * @author LovesAsuna
 **/
open class SimplifyDetector : Detector {
    private val dfaPathMap: MutableMap<NonTerm, Boolean> = mutableMapOf()
    private var hasCycle = false
    private val pathStack: Deque<NonTerm> = LinkedList()

    override fun detect(grammar: Grammar): Grammar {
        removeRedundant(grammar)
        // init pathMap
        grammar.rules.keys.forEach {
            dfaPathMap[it] = false
        }
        removeHarmful(grammar, grammar.head)
        return grammar
    }

    private fun removeHarmful(grammar: Grammar, nonTerm: NonTerm) {
        val value = grammar.rules[nonTerm]
        if (dfaPathMap[nonTerm]!!) {
            val set = mutableSetOf<NonTerm>()
            if (pathStack.first == nonTerm) {
                set.add(pathStack.first)
            } else {
                while (pathStack.first != nonTerm) {
                    set.add(pathStack.pop())
                }
                set.add(pathStack.first)
            }
            if (set.stream().noneMatch { hasTermOrEmpty(grammar, it, grammar.rules[it]!!) }) {
                grammar.rules.keys.removeAll(set)
                fun removeInvalidValue() {
                    grammar.rules.values.forEach { outer ->
                        outer.removeIf { inner ->
                            inner.stream().anyMatch {
                                set.contains(it) || (it is NonTerm && it !in grammar.rules.keys)
                            }
                        }
                    }
                }
                fun removeEmptyValue() {
                    grammar.rules.values.remove(grammar.rules.values.stream().filter {
                        it.isEmpty()
                    }.findFirst().orElseGet { null })
                }
                var size = 0
                while (size != grammar.rules.size) {
                    size = grammar.rules.size
                    removeInvalidValue()
                    removeEmptyValue()
                }
            }
            set.clear()
            return
        }
        dfaPathMap[nonTerm] = true
        pathStack.push(nonTerm)
        val flattenList = value!!.flatten()
        for (element in flattenList) {
            if (element is NonTerm) {
                removeHarmful(grammar, element)
                if (hasCycle) {
                    return
                }
            }
        }
        dfaPathMap[nonTerm] = false
    }

    private fun hasTermOrEmpty(grammar: Grammar, left: NonTerm, list: List<List<Symbol>>): Boolean {
        if (list.isEmpty()) {
            return false
        }
        if (
            list.stream().anyMatch {
                it.first() is Term || it.first() is Empty
            }
        ) {
            return true
        }
        var flag = false
        for (e in list) {
            if (e.first() == left) continue
            flag = flag || hasTermOrEmpty(grammar, e.first() as NonTerm, grammar.rules[e.first() as NonTerm]!!)
            if (flag) {
                return true
            }
        }
        return flag
    }

    private fun removeRedundant(grammar: Grammar) {
        val left = grammar.rules.keys
        val right = grammar.rules.values.flatMap { outer ->
            outer.flatten().filterIsInstance<NonTerm>()
        }
        val redundantSet = mutableSetOf<NonTerm>().apply {
            addAll(left)
            addAll(right)
            removeAll(left.toMutableSet().also { it.retainAll(right) })
            remove(grammar.head)
        }
        redundantSet.forEach {
            grammar.rules.remove(it)
        }
    }
}