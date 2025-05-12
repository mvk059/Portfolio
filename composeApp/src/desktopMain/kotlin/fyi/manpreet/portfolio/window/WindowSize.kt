package fyi.manpreet.portfolio.window

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
actual fun calculateWindowWidthSize(): WindowWidthSizeClass {
    val windowSizeClass = calculateWindowSizeClass()
    return windowSizeClass.widthSizeClass
}