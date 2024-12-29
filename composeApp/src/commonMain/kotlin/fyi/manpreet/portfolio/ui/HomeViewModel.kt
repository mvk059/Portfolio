package fyi.manpreet.portfolio.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class Apps(val title: String, val subtitle: String, val type: AppType)

enum class AppType {
    STARFIELD,
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

    private fun initAppsList() {
        _apps.update {
            listOf(
                Apps(
                    title = "StarField",
                    subtitle = "StarField or WrapSpeed visualisation in space",
                    type = AppType.STARFIELD,
                )
            )
        }
    }
}