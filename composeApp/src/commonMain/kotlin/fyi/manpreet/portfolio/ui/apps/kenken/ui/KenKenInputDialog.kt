package fyi.manpreet.portfolio.ui.apps.kenken.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogProperties
import com.composables.core.rememberDialogState
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenCellValue
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenGridIntent
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenOperation
import fyi.manpreet.portfolio.ui.apps.kenken.model.KenKenShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KenKenInputDialog(
    shape: KenKenShape,
    onShapeUpdate: (KenKenGridIntent.UpdateShape) -> Unit,
) {
    val dialogState = rememberDialogState(initiallyVisible = true)
    var operator by remember { mutableStateOf(shape.operator.operation) }
    var valueText by remember { mutableStateOf(shape.operator.targetValue.value.toString()) }

    Box {
        Dialog(
            state = dialogState,
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        ) {
            DialogPanel(
                modifier = Modifier
                    .displayCutoutPadding()
                    .systemBarsPadding()
                    .widthIn(min = 280.dp, max = 560.dp)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFE4E4E4), RoundedCornerShape(12.dp))
                    .background(Color.White),
            ) {

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        KenKenOperation.entries.forEach { operation ->
                            OutlinedIconButton(
                                onClick = { operator = operation },
                                colors = IconButtonDefaults.outlinedIconButtonColors().copy(
                                    containerColor = if (operator.symbol == operation.symbol) Color.Blue else IconButtonDefaults.outlinedIconButtonColors().containerColor,
                                    contentColor = if (operator.symbol == operation.symbol) Color.White else IconButtonDefaults.outlinedIconButtonColors().contentColor,
                                ),
                            ) {
                                Text(operation.symbol)
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    // TODO Callback when enter is clicked
                    OutlinedTextField(
                        value = valueText,
                        onValueChange = { valueText = it },
                        label = { Text("Target Value") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { onShapeUpdate(KenKenGridIntent.UpdateShape(shape = shape, operation = operator, targetValue = KenKenCellValue(valueText.toIntOrNull() ?: 0))) },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Update",
                        )
                    }
                }
            }
        }
    }
}
