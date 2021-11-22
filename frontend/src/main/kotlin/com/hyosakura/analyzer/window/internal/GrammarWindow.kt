package com.hyosakura.analyzer.window.internal

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.hyosakura.analyzer.grammar.Grammar

@OptIn(ExperimentalUnitApi::class)
@Composable
fun GrammarWindow(grammar: Grammar) {
    OutlinedTextField(
        value = grammar.toString(),
        onValueChange = {},
        readOnly = true,
        textStyle = TextStyle(fontSize = TextUnit(20F, TextUnitType.Sp)),
        modifier = Modifier.fillMaxSize()
    )
}