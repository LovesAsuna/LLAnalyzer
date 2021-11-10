package com.hyosakura.analyzer.grammar.detector

import com.hyosakura.analyzer.parser.GrammarScanner
import org.junit.jupiter.api.Test

internal class LeftCommonFactorDetectorTest {

    @Test
    fun detect() {
        val str = "S -> cAd | cB\nA -> ab | a\nB -> aa"
        val grammar = GrammarScanner().parse(str)
        val detector = LeftCommonFactorDetector()
        detector.detect(grammar)
    }
}