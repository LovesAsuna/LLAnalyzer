package com.hyosakura.analyzer.window

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import com.hyosakura.analyzer.grammar.Grammar
import com.hyosakura.analyzer.grammar.analyzer.Analyzer
import com.hyosakura.analyzer.grammar.analyzer.LLTable
import kotlinx.coroutines.CompletableDeferred

class MainWindowState(
    private val exit: MainWindowState.() -> Unit
) {
    var grammarText by mutableStateOf("")

    var grammar by mutableStateOf<Grammar?>(null)

    var table by mutableStateOf<LLTable?>(null)

    var analyzer by mutableStateOf<Analyzer?>(null)

    var statement by mutableStateOf("")

    val window = WindowState(height = 800.dp)

    var error by mutableStateOf<Throwable?>(null)

    var showType by mutableStateOf(ShowType.CLEAR)

    fun exit() = exit(this)
}

enum class ShowType {
    ERROR,
    CLEAR,
    PARSED,
    SIMPLIFY,
    LEFT_COMMON_FACTOR,
    LEFT_RECURSIVE,
    FIRST,
    FOLLOW,
    RESULT
}

class DialogState<T> {
    private var onResult: CompletableDeferred<T>? by mutableStateOf(null)

    val isAwaiting get() = onResult != null

    suspend fun awaitResult(): T {
        onResult = CompletableDeferred()
        val result = onResult!!.await()
        onResult = null
        return result
    }

    fun onResult(result: T) = onResult!!.complete(result)
}