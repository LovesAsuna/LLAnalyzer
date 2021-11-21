package com.hyosakura.analyzer.grammar.analyzer

import com.hyosakura.analyzer.parser.GrammarScanner
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DefaultAnalyzerTest {

    @Test
    fun analyze() {
        val str = """
              S -> AbB | Bc 
              A -> aA | ε
              B -> d | e
        """.trimIndent()
        val grammar = GrammarScanner().parse(str)
        val table = LLTable(grammar)
        val analyzer = DefaultAnalyzer(table)
        assertTrue(analyzer.analyze("abd"))
    }
}