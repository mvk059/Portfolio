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

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
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