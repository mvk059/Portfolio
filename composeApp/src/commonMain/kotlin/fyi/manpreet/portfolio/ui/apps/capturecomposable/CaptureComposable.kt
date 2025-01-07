package fyi.manpreet.portfolio.ui.apps.capturecomposable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CaptureComposable(
    textColor: Color = Color.Black,
) {

    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    var text by remember { mutableStateOf("") }
    var capturedBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "To Capture Compose",
            modifier = Modifier.padding(bottom = 16.dp),
            color = textColor,
        )

        Box(
            modifier = Modifier
                .drawWithContent {
                    // call record to capture the content in the graphics layer
                    graphicsLayer.record {
                        // draw the contents of the composable into the graphics layer
                        this@drawWithContent.drawContent()
                    }
                    // draw the graphics layer on the visible canvas
                    drawLayer(graphicsLayer)
                }
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter text") },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    capturedBitmap = graphicsLayer.toImageBitmap()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save", color = textColor)
        }

        // Separator
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            thickness = 5.dp
        )

        Text(
            text = "Captured Compose",
            modifier = Modifier.padding(bottom = 16.dp),
            color = textColor,
        )

        // Image Display
        val bitmap = capturedBitmap
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "Captured Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Fit
            )
        }
    }


}