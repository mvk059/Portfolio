package fyi.manpreet.portfolio.ui.apps.expandable_text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle

private const val sampleText =
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32."

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String = sampleText,
    collapsedMaxLine: Int = 3,
    showMoreText: String = " Show More",
    showLessText: String = " Show Less",
    style: TextStyle = MaterialTheme.typography.bodySmall,
    textColor: Color = Color.Black,
    showMoreStyle: SpanStyle = SpanStyle(color = Color.Blue, fontWeight = FontWeight.W500),
    showLessStyle: SpanStyle = SpanStyle(color = Color.Blue, fontWeight = FontWeight.W500),
) {

    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }
    var lastCharacterIndex by remember { mutableStateOf(0) }

    val annotatedText = buildAnnotatedString {
        if (isClickable) {
            if (isExpanded) {
                append(text)
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Show Less",
                        linkInteractionListener = { isExpanded = !isExpanded }
                    )
                ) {
                    withStyle(style = showLessStyle) {
                        append(showLessText)
                    }
                }
            } else {
                val adjustText = text.substring(startIndex = 0, endIndex = lastCharacterIndex)
                    .dropLast(showMoreText.length)
                    .dropLastWhile { it.isWhitespace() || it == '.' }
                append(adjustText)
                withLink(
                    link = LinkAnnotation.Clickable(
                        tag = "Show More",
                        linkInteractionListener = { isExpanded = !isExpanded }
                    )
                ) {
                    withStyle(style = showMoreStyle) {
                        append(showMoreText)
                    }
                }
            }
        } else {
            append(text)
        }
    }

    Text(
        text = annotatedText,
        modifier = modifier,
        style = style,
        color = textColor,
        maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
        onTextLayout = { textLayoutResult ->
            if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                isClickable = true
                lastCharacterIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
            }
        },
    )
}