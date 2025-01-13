package fyi.manpreet.portfolio.ui.apps.transcibe_audio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TranscribeAudio(
    modifier: Modifier = Modifier,
    viewModel: TranscribeAudioViewModel = TranscribeAudioViewModel()
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    TranscribeAudioContent(
        modifier = modifier,
        state  = state,
        onTranscribeClick = viewModel::transcribe
    )
}

@Composable
private fun TranscribeAudioContent(
    modifier: Modifier = Modifier,
    state: State<TranscribeAudioState>,
    onTranscribeClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Button(onClick = onTranscribeClick) {
                Text(text = "Transcribe Audio")
            }

            if (state.value.transcribeAudio != null) {
                Text(
                    text = state.value.transcribeAudio ?: "",
                    color = Color.Blue,
                    fontSize = 20.sp,
                )
            }

            if (state.value.error != null) {
                Text(
                    text = state.value.error ?: "",
                    color = Color.Red,
                    fontSize = 20.sp,
                )
            }
        }

        if (state.value.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp).align(Alignment.Center),
                    color = Color.Red,
                )
            }
        }
    }
}