package com.hyosakura.analyzer.grammar.analyzer

/**
 * @author LovesAsuna
 **/
interface Analyzer {
    class DeriveResult {
        data class Row(
            val step: Int,
            val opStack: String,
            val inputText: String,
            var action: String = ""
        )

        val rows = mutableListOf<Row>()
    }

    val table: LLTable

    val result : DeriveResult

    fun analyze(text: String): Boolean
}