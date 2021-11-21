package com.hyosakura.analyzer.grammar.analyzer

/**
 * @author LovesAsuna
 **/
interface Analyzer {
    fun analyze(text: String): Boolean
}