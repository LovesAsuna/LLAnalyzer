package com.hyosakura.analyzer

import androidx.compose.runtime.Composable
import com.hyosakura.analyzer.window.MainWindow

@Composable
fun Application(state: ApplicationState) {
    MainWindow(state.mainWindowState)
}