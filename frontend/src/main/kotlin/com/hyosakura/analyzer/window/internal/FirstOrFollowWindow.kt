package com.hyosakura.analyzer.window.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hyosakura.analyzer.grammar.Grammar
import com.hyosakura.analyzer.grammar.Symbol
import com.hyosakura.analyzer.window.surfaceText

/**
 * @author LovesAsuna
 **/
@Composable
fun FirstOrFollowWindow(grammar: Grammar, set: Map<Symbol, Set<Symbol>>, setName: String) {
    val stateHorizontal = rememberScrollState(0)
    Column(modifier = Modifier.verticalScroll(stateHorizontal)) {
        Row {
            surfaceText("NonTerm", Modifier.weight(1F))
            surfaceText(setName, Modifier.weight(1F))
        }
        Spacer(modifier = Modifier.height(10.dp).background(Color.White))
        grammar.rules.forEach {
            Row {
                surfaceText(it.key.symbol, Modifier.weight(1F))
                surfaceText(set[it.key]!!.joinToString(", "), Modifier.weight(1F))
            }
        }
    }
}
