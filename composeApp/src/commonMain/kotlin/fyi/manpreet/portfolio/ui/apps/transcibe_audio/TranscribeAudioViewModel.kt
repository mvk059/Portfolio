package fyi.manpreet.portfolio.ui.apps.transcibe_audio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.vyfor.groqkt.GroqClient
import io.github.vyfor.groqkt.GroqModel
import io.github.vyfor.groqkt.api.GroqResponse
import io.github.vyfor.groqkt.api.audio.transcription.AudioTranscription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TranscribeAudioState(
    val isLoading: Boolean = false,
    val transcribeAudio: String? = null,
    val error: String? = null
)

class TranscribeAudioViewModel : ViewModel() {

    private val _state = MutableStateFlow(TranscribeAudioState())
    val state = _state.asStateFlow()

    private val client: GroqClient by lazy {
        GroqClient(apiKey = getApiKey())
    }

    fun transcribe() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {

            println("transcribe viewModelScope")
            val result: Result<GroqResponse<AudioTranscription>> = client.transcribeAudio {
                model = GroqModel.DISTIL_WHISPER_LARGE_V3_EN
                filename = "Audio"
                url =
                    "https://cdn.pixabay.com/download/audio/2024/08/04/audio_be9247e137.mp3?filename=girl-ix27ve-never-been-out-of-the-village-before-229855.mp3"
            }
            if (result.isFailure) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
                return@launch
            }
            if (result.isSuccess) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        transcribeAudio = result.getOrNull()?.data?.text
                    )
                }
            }
        }
    }
}
