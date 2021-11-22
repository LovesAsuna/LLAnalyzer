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
import com.hyosakura.analyzer.grammar.analyzer.Analyzer
import com.hyosakura.analyzer.window.surfaceText

@Composable
fun DeriveResultWindow(analyzer: Analyzer) {
    val stateHorizontal = rememberScrollState(0)
    Column(modifier = Modifier.verticalScroll(stateHorizontal)) {
        Row {
            surfaceText("步骤", Modifier.weight(1F))
            surfaceText("符号栈", Modifier.weight(1F))
            surfaceText("输入串", Modifier.weight(1F))
            surfaceText("动作", Modifier.weight(1F))
        }
        Spacer(modifier = Modifier.height(10.dp).background(Color.White))
        val result = analyzer.result
        result.rows.forEach {
            Row {
                surfaceText(it.step.toString(), Modifier.weight(1F))
                surfaceText(it.opStack, Modifier.weight(1F))
                surfaceText(it.inputText, Modifier.weight(1F))
                surfaceText(it.action, Modifier.weight(1F))
            }
            Spacer(modifier = Modifier.height(10.dp).background(Color.White))
        }
    }
}
