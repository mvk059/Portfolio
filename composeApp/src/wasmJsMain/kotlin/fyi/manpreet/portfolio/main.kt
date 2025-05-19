package fyi.manpreet.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import fyi.manpreet.portfolio.ui.apps.capturecomposable.CaptureComposable
import fyi.manpreet.portfolio.ui.apps.expandable_text.ExpandableText
import fyi.manpreet.portfolio.ui.apps.filterchip_dropdown.FilterChipDropdown
import fyi.manpreet.portfolio.ui.apps.starfield.StarField
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {

        // Observe window size changes
        val windowSize = remember { mutableStateOf(getWindowSize()) }
        val canvasSize = remember { mutableStateOf(getCanvasSize()) }
        listenForWindowResize {
            windowSize.value = it
        }

        BoxWithConstraints(
            modifier = Modifier.size(canvasSize.value.first, canvasSize.value.second)
        ) {
            composeApp()
        }
    }
}

@Composable
fun composeApp() {
    // Display the hovered section
    val hoverItem = remember { mutableStateOf(1) }

    observeHoverState { newValue ->
        hoverItem.value = newValue
    }

    when (hoverItem.value) {
        1 -> StarField()

        2 -> ProjectNavigationCard(
            projectTitle = "Flow Diary",
            projectDescription = "Audio journaling app to capture your wonderful thoughts!",
            projectUrl = "https://github.com/mvk059/FlowDiary"
        )

        3 -> ProjectNavigationCard(
            projectTitle = "Meme Editor",
            projectDescription = "Transform your ideas into viral memes! Featuring popular templates, custom text styles, and easy to download memes.\n\nJump in to start your meme-making journey!",
            projectUrl = "https://meme.manpreet.fyi"
        )

        4 -> ProjectNavigationCard(
            projectTitle = "Bright Start",
            projectDescription = "An alarm with a cool custom time picker. Schedule your alarms for specific day and time.",
            projectUrl = "https://github.com/mvk059/BrightStart"
        )

        5 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFF1a1a1a), Color(0xFF2a0a2a))))
                    .padding(8.dp)
            ) {
                CaptureComposable(textColor = Color.White)
            }
        }

        6 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFF1a1a1a), Color(0xFF2a0a2a))))
                    .padding(8.dp)
            ) {
                ExpandableText(
                    textColor = Color.White,
                    showMoreStyle = SpanStyle(color = Color.Cyan, fontWeight = FontWeight.W500),
                    showLessStyle = SpanStyle(color = Color.Cyan, fontWeight = FontWeight.W500),
                )
            }
        }

        7 -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFF1a1a1a), Color(0xFF2a0a2a))))
                    .padding(8.dp)
            ) {
                FilterChipDropdown()
            }
        }


    }
}

private fun observeHoverState(onHoverItemChanged: (Int) -> Unit) {
    // Set up hover handlers for each section
    for (i in 1..7) {
        document.getElementById("section$i")?.let { section ->
            (section as HTMLElement).onmouseenter = {
                onHoverItemChanged(i)
            }
        }
    }
}

fun getCanvasSize(): Pair<Dp, Dp> {
    val container = document.getElementById("canvasContainer") as HTMLElement
    val rect = container.getBoundingClientRect()
    val width = Dp(rect.width.toFloat())
    val height = Dp(rect.height.toFloat())
    return width to height
}

@Composable
private fun listenForWindowResize(onSizeChanged: (Pair<Int, Int>) -> Unit) {
    DisposableEffect(Unit) {
        val resizeListener: (Event) -> Unit = {
            onSizeChanged(getWindowSize())
        }
        window.addEventListener("resize", resizeListener)
        onDispose {
            window.removeEventListener("resize", resizeListener)
        }
    }
}

private fun getWindowSize(): Pair<Int, Int> {
    return Pair(window.innerWidth, window.innerHeight)
}