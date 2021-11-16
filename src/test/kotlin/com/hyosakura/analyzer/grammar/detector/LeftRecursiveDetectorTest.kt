package com.hyosakura.analyzer.grammar.detector

import com.hyosakura.analyzer.parser.GrammarScanner
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class LeftRecursiveDetectorTest {

    @Test
    fun detect() {
        val str = "S -> A\nA ->Aa|b"
        val grammar = GrammarScanner().parse(str)
        val detector = LeftRecursiveDetector()
        detector.detect(grammar)
    }
}