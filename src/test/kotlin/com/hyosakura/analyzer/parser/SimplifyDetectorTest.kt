package com.hyosakura.analyzer.parser

import com.hyosakura.analyzer.grammar.detector.SimplifyDetector
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SimplifyDetectorTest {

    @Test
    fun detect() {
        val str = "S -> Be\nB -> Ce\nB -> Af\nA -> Ae\nA->e\nC->Cf\nD->f"
        // val str = "S -> A\nA -> B\nB -> C\nC -> A\n"
        // val str = "S -> A\nA -> BC\nB -> B\nC -> C\n"
        // val str = "A -> Af\nA -> f"
        val grammar = GrammarScanner().parse(str)
        val detector = SimplifyDetector()
        detector.detect(grammar)
    }
}