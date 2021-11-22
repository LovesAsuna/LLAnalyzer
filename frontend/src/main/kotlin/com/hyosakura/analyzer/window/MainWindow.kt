package com.hyosakura.analyzer.window

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import com.hyosakura.analyzer.grammar.analyzer.DefaultAnalyzer
import com.hyosakura.analyzer.grammar.analyzer.DefaultTable
import com.hyosakura.analyzer.grammar.detector.LeftCommonFactorDetector
import com.hyosakura.analyzer.grammar.detector.LeftRecursiveDetector
import com.hyosakura.analyzer.grammar.detector.SimplifyDetector
import com.hyosakura.analyzer.parser.GrammarScanner
import com.hyosakura.analyzer.window.internal.ClearWindow
import com.hyosakura.analyzer.window.internal.DeriveResultWindow
import com.hyosakura.analyzer.window.internal.FirstOrFollowWindow
import com.hyosakura.analyzer.window.internal.GrammarWindow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@OptIn(ExperimentalUnitApi::class)
@Composable
fun MainWindow(state: MainWindowState) {
    val scope = rememberCoroutineScope()

    fun exit() = scope.launch { state.exit() }

    Window(
        state = state.window,
        title = "GrammarAnalyzer",
        resizable = false,
        onCloseRequest = { exit() }
    ) {
        Column(modifier = Modifier.background(Color(222, 222, 222))) {
            Row(modifier = Modifier.weight(0.25F).offset(y = 5.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxSize().background(Color(240, 240, 240)).weight(1F),
                    label = {
                        Text(
                            text = "Grammar",
                            fontSize = TextUnit(20F, TextUnitType.Sp),
                        )
                    },
                    placeholder = {
                        Text("文法")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = TextUnit(15F, TextUnitType.Sp)),
                    value = state.grammarText,
                    onValueChange = {
                        state.grammarText = it
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxSize().background(Color(240, 240, 240)).weight(1F),
                    label = {
                        Text(
                            text = "Statement",
                            fontSize = TextUnit(20F, TextUnitType.Sp),
                        )
                    },
                    placeholder = {
                        Text("待分析句子")
                    },
                    textStyle = TextStyle.Default.copy(fontSize = TextUnit(15F, TextUnitType.Sp)),
                    value = state.statement,
                    maxLines = 1,
                    onValueChange = {
                        state.statement = it
                    }
                )
            }
            Row(
                modifier = Modifier.padding(5.dp).background(Color(200, 200, 200)).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    clear(state)
                }) {
                    Text("清空")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    parse(state)
                }) {
                    Text("解析")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = {
                    scope.launch {
                        simplify(state)
                    }
                }) {
                    Text("化简")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = {
                    scope.launch {
                        leftCommonFactor(state)
                    }
                }) {
                    Text("提取左公因子")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = {
                    scope.launch {
                        leftRecursive(state)
                    }
                }) {
                    Text("消除左递归")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = {
                    scope.launch {
                        first(state)
                    }
                }) {
                    Text("First集合")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = {
                    scope.launch {
                        follow(state)
                    }
                }) {
                    Text("Follow集合")
                }
                Spacer(modifier = Modifier.width(5.dp))
                Button(onClick = {
                    scope.launch {
                        analyze(state)
                    }
                }) {
                    Text("最左推导")
                }
            }
            Row(modifier = Modifier.background(Color(240, 240, 240)).weight(0.5F)) {
                // 显示
                when (state.showType) {
                    ShowType.ERROR -> {
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        state.error!!.printStackTrace(PrintStream(byteArrayOutputStream))
                        OutlinedTextField(
                            value = String(byteArrayOutputStream.toByteArray()).replace("\t", " ".repeat(4)),
                            onValueChange = {},
                            readOnly = true,
                            textStyle = TextStyle(fontSize = TextUnit(20F, TextUnitType.Sp), color = Color.Red),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    ShowType.CLEAR -> ClearWindow()
                    ShowType.PARSED, ShowType.SIMPLIFY, ShowType.LEFT_COMMON_FACTOR, ShowType.LEFT_RECURSIVE -> GrammarWindow(
                        state.grammar!!
                    )
                    ShowType.FIRST -> FirstOrFollowWindow(state.grammar!!, state.grammar!!.first, "First")
                    ShowType.FOLLOW -> FirstOrFollowWindow(state.grammar!!, state.grammar!!.follow, "Folow")
                    ShowType.RESULT -> DeriveResultWindow(state.analyzer!!)
                }
            }
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun surfaceText(text: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        border = BorderStroke(1.dp, SolidColor(MaterialTheme.colors.primarySurface)),
        color = Color(240, 240, 240)
    ) {
        Text(
            text = text,
            fontSize = TextUnit(25F, TextUnitType.Sp),
            textAlign = TextAlign.Center
        )
    }
}

fun error(state: MainWindowState) {
    state.showType = ShowType.ERROR
}

fun clear(state: MainWindowState) {
    state.grammarText = ""
    state.statement = ""
    state.showType = ShowType.CLEAR
}

fun parse(state: MainWindowState) {
    runCatching {
        val scanner = GrammarScanner()
        state.grammar = scanner.parse(state.grammarText)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.PARSED
    }
}

fun simplify(state: MainWindowState) {
    runCatching {
        require(state.showType >= ShowType.PARSED) { "请先进行语法分析" }
        val detector = SimplifyDetector()
        detector.detect(state.grammar!!)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.SIMPLIFY
    }
}

fun leftCommonFactor(state: MainWindowState) {
    runCatching {
        require(state.showType >= ShowType.SIMPLIFY) { "请先进行语法化简" }
        val detector = LeftCommonFactorDetector()
        detector.detect(state.grammar!!)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.LEFT_COMMON_FACTOR
    }
}

fun leftRecursive(state: MainWindowState) {
    runCatching {
        require(state.showType >= ShowType.LEFT_COMMON_FACTOR) { "请先进行公因子提取" }
        val detector = LeftRecursiveDetector()
        detector.detect(state.grammar!!)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.LEFT_RECURSIVE
    }
}

fun first(state: MainWindowState) {
    runCatching {
        require(state.showType >= ShowType.LEFT_RECURSIVE) { "请先进行左递归消除" }
        state.grammar!!.getFirst()
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.FIRST
    }
}

fun follow(state: MainWindowState) {
    runCatching {
        require(state.showType >= ShowType.LEFT_RECURSIVE) { "请先进行左递归消除" }
        state.grammar!!.getFollow()
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.FOLLOW
    }
}

fun analyze(state: MainWindowState) {
    runCatching {
        require(state.showType >= ShowType.FOLLOW) { "请先计算Follow集合" }
        if (state.statement.isEmpty()) return
        state.table = DefaultTable(state.grammar!!)
        state.analyzer = DefaultAnalyzer(state.table!!)
        state.analyzer!!.analyze(state.statement)
    }.onFailure {
        state.error = it
        error(state)
    }.onSuccess {
        state.showType = ShowType.RESULT
    }
}