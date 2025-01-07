package fyi.manpreet.portfolio.ui.apps.filterchip_dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun FilterChipDropdown(
    modifier: Modifier = Modifier,
) {

    var selectedTopics by remember { mutableStateOf(setOf<String>()) }
    var savedTopics by remember { mutableStateOf(setOf("Work", "Hobby", "Personal", "Office", "Workout")) }
    var isAddingTopic by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        TopicsList(
            modifier = modifier,
            selectedTopics = selectedTopics,
            onSelectedTopics = { selectedTopics = it },
            isAddingTopic = isAddingTopic,
            onAddingTopicChange = { isAddingTopic = it },
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            keyboardController = keyboardController,
            focusRequester = focusRequester,
        )

        if (searchQuery.isEmpty()) return

        TopicDropdown(
            modifier = modifier,
            selectedTopics = selectedTopics,
            onSelectedTopicsChange = { selectedTopics = it },
            onAddingTopicChange = { isAddingTopic = it },
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            savedTopics = savedTopics,
            onSavedTopicsChange = { savedTopics = it },
        )
    }
}

@Composable
private fun TopicsList(
    modifier: Modifier = Modifier,
    selectedTopics: Set<String>,
    onSelectedTopics: (Set<String>) -> Unit,
    isAddingTopic: Boolean,
    onAddingTopicChange: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusRequester: FocusRequester,
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                spotColor = Color(0xFF474F60).copy(alpha = 0.08f)
            ),
        color = Color(0xFFD9E2FF),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            TitleText()

            ChipFlowRow(
                selectedTopics = selectedTopics,
                onSelectedTopics = onSelectedTopics,
                isAddingTopic = isAddingTopic,
                onAddingTopicChange = onAddingTopicChange,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                keyboardController = keyboardController,
                focusRequester = focusRequester,
            )
        }
    }
}

@Composable
private fun TitleText() {

    Text(
        text = "Topics",
        style = MaterialTheme.typography.titleMedium,
        color = Color(0xFF191A20),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipFlowRow(
    modifier: Modifier = Modifier,
    selectedTopics: Set<String>,
    onSelectedTopics: (Set<String>) -> Unit,
    isAddingTopic: Boolean,
    onAddingTopicChange: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusRequester: FocusRequester,
) {

    LaunchedEffect(isAddingTopic) {
        if (isAddingTopic.not()) return@LaunchedEffect
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // Selected Topics
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {

        selectedTopics.forEach { topic ->
            TopicChip(
                modifier = Modifier.padding(end = 4.dp),
                topic = topic,
                onCancel = { onSelectedTopics(selectedTopics - topic) }
            )
        }

        if (isAddingTopic.not()) {
            IconButton(
                onClick = { onAddingTopicChange(true) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(color = Color(0xFFEEF0FF), shape = CircleShape)
                    .size(32.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color(0xFF40434F),
                    )
                }
            )
        } else {
            BasicTextField(
                value = searchQuery,
                onValueChange = { onSearchQueryChange(it) },
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp)
                    .focusRequester(focusRequester),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAddingTopicChange(false)
                        keyboardController?.hide()
                    }
                )
            )
        }
    }
}

@Composable
fun TopicChip(
    modifier: Modifier = Modifier,
    topic: String,
    onCancel: () -> Unit = {},
) {

    FilterChip(
        modifier = modifier,
        selected = true,
        onClick = {},
        shape = MaterialTheme.shapes.large,
        colors = FilterChipDefaults.filterChipColors().copy(selectedContainerColor = Color(0xFFEEF0FF)),
        leadingIcon = {
            Icon(
                Icons.Outlined.Info,
                modifier = Modifier.size(16.dp),
                contentDescription = null,
                tint = Color.DarkGray,
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.Close,
                modifier = Modifier.size(16.dp).clickable { onCancel() },
                contentDescription = "Cancel",
                tint = Color.DarkGray,
            )
        },
        label = {
            Text(
                text = topic,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF40434F),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    )
}

@Composable
fun TopicDropdown(
    modifier: Modifier = Modifier,
    selectedTopics: Set<String>,
    onSelectedTopicsChange: (Set<String>) -> Unit,
    onAddingTopicChange: (Boolean) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    savedTopics: Set<String>,
    onSavedTopicsChange: (Set<String>) -> Unit = {},
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .offset(y = -(8.dp))
            .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.medium),
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.White)
        ) {

            // Filter saved topics based on search query
            val matchingSavedTopics = savedTopics.filter {
                it.startsWith(prefix = searchQuery, ignoreCase = true) && !selectedTopics.contains(it)
            }

            // Show matching saved topics first
            matchingSavedTopics.forEach { topic ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                onSelectedTopicsChange(selectedTopics + topic)
                                onSearchQueryChange("")
                                onAddingTopicChange(false)
                            }
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        text = topic,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xFF40434F),
                    )
                }
            }

            // Show create option if query doesn't exist in saved topics
            if (!savedTopics.any { it.equals(searchQuery, ignoreCase = true) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val newTopic = searchQuery.trim()
                            onSavedTopicsChange(savedTopics + newTopic)
                            onSelectedTopicsChange(selectedTopics + newTopic)
                            onSearchQueryChange("")
                            onAddingTopicChange(false)
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        modifier = Modifier.size(12.dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        text = "Create $searchQuery",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Black,
                    )
                }
            }
        }
    }
}
