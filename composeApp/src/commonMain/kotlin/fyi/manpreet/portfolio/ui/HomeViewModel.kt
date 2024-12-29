package fyi.manpreet.portfolio.ui

import androidx.compose.material.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Apps(val title: String, val subtitle: String, val type: AppType)

enum class AppType {
    EMPTY,
    STARFIELD,
    CAPTURE_COMPOSABLE,
    COMPOSABLE_MEME,
    BRIGHT_START
}

class HomeViewModel : ViewModel() {

    private val _apps = MutableStateFlow<List<Apps>>(emptyList())
    val apps = _apps
        .onStart { initAppsList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    val snackbarHostState = SnackbarHostState()

    private fun initAppsList() {
        _apps.update {
            listOf(
                Apps(title = "", subtitle = "", type = AppType.EMPTY),
                Apps(title = "", subtitle = "", type = AppType.EMPTY),
                Apps(title = "", subtitle = "", type = AppType.EMPTY),
                Apps(title = "", subtitle = "", type = AppType.EMPTY),
                Apps(
                    title = "StarField",
                    subtitle = "StarField or WrapSpeed visualisation in space",
                    type = AppType.STARFIELD,
                ),
                Apps(
                    title = "Capture Composable",
                    subtitle = "Turn any composable into an ImageBitmap",
                    type = AppType.CAPTURE_COMPOSABLE,
                ),
                Apps(
                    title = "Composable Meme",
                    subtitle = "Meme Maker",
                    type = AppType.COMPOSABLE_MEME,
                ),
                Apps(
                    title = "BrightStart",
                    subtitle = "An Alarm app",
                    type = AppType.BRIGHT_START,
                ),
                Apps(title = "", subtitle = "", type = AppType.EMPTY),
                Apps(title = "", subtitle = "", type = AppType.EMPTY),
                Apps(title = "", subtitle = "", type = AppType.EMPTY),
                Apps(title = "", subtitle = "", type = AppType.EMPTY),
            )
        }
    }

    fun showSnackBar(type: AppType) {
        viewModelScope.launch {
            when (type) {
                AppType.BRIGHT_START -> snackbarHostState.showSnackbar("Visit github.com/mvk059 to view this")
                AppType.COMPOSABLE_MEME -> snackbarHostState.showSnackbar("Visit meme.manpreet.fyi to view this")
                else -> {}
            }
        }
    }
}