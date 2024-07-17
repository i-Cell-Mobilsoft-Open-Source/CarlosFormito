package com.icell.external.carlosformito.ui.field

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.KeyboardAlt
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.extension.selectedTime
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.field.base.TextFieldInputMode
import com.icell.external.carlosformito.ui.field.base.TrackVisibilityEffect
import com.icell.external.carlosformito.ui.theme.LocalCarlosFormats
import com.icell.external.carlosformito.ui.theme.LocalCarlosIcons
import com.icell.external.carlosformito.ui.util.onFocusCleared
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun FormTimePickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<LocalTime>,
    label: String,
    timeFormatter: DateTimeFormatter = LocalCarlosFormats.current.timeFormatter,
    dialogTitle: String = label,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: (() -> Unit)? = null,
    is24HourFormat: Boolean = LocalCarlosFormats.current.is24HourFormat,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    customErrorMessage: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val state by fieldItem.collectFieldState()
    val isError = state.isError || !customErrorMessage.isNullOrBlank()
    FormTimePickerField(
        modifier = modifier,
        value = state.value,
        label = label,
        timeFormatter = timeFormatter,
        onValueChange = { value ->
            fieldItem.onFieldValueChanged(value)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        },
        onVisibilityChanged = { visible ->
            fieldItem.onFieldVisibilityChanged(visible)
        },
        dialogTitle = dialogTitle,
        leadingContentType = leadingContentType,
        isError = isError,
        enabled = enabled,
        isClearable = isClearable,
        onClick = onClick,
        is24HourFormat = is24HourFormat,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        contentDescription = contentDescription,
        supportingText = if (isError) {
            customErrorMessage ?: state.errorMessage() ?: supportingText
        } else {
            supportingText
        },
        testTag = testTag
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormTimePickerField(
    modifier: Modifier = Modifier,
    value: LocalTime?,
    label: String,
    timeFormatter: DateTimeFormatter = LocalCarlosFormats.current.timeFormatter,
    onValueChange: (LocalTime?) -> Unit,
    dialogTitle: String = label,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: (() -> Unit)? = null,
    onFocusCleared: () -> Unit = {},
    onVisibilityChanged: (visible: Boolean) -> Unit = {},
    is24HourFormat: Boolean = LocalCarlosFormats.current.is24HourFormat,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val context = LocalContext.current
    val carlosIcons = LocalCarlosIcons.current

    TrackVisibilityEffect(onVisibilityChanged)

    val displayMode = remember { mutableStateOf(DisplayMode.Picker) }
    var dialogVisible by remember { mutableStateOf(false) }

    if (dialogVisible) {
        val currentValue = value ?: LocalTime.now()
        val timePickerState = rememberTimePickerState(
            initialHour = currentValue.hour,
            initialMinute = currentValue.minute,
            is24Hour = is24HourFormat
        )

        TimePickerDialog(
            onDismissRequest = { dialogVisible = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onValueChange(timePickerState.selectedTime)
                        dialogVisible = false
                    }
                ) {
                    Text(text = context.getString(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { dialogVisible = false }
                ) {
                    Text(text = context.getString(android.R.string.cancel))
                }
            },
            displayModeIcon = {
                ChangeLayoutIcon(displayMode)
            }
        ) {
            TimePicker(
                timePickerState = timePickerState,
                displayMode = displayMode,
                title = dialogTitle
            )
        }
    }

    BaseTextField(
        modifier = modifier.onFocusCleared(onFocusCleared),
        value = value?.let { timeFormatter.format(value) } ?: "",
        label = label,
        enabled = enabled,
        isError = isError,
        trailingContentType = TextFieldAffixContentType.Icon(
            value = if (isClearable && value != null) {
                carlosIcons.clear
            } else {
                carlosIcons.schedule
            }
        ),
        leadingContentType = leadingContentType,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        contentDescription = contentDescription,
        visualTransformation = visualTransformation,
        supportingText = supportingText,
        testTag = testTag,
        onValueChange = {
            // intentionally blank
        },
        inputMode = TextFieldInputMode.Picker(
            onClick = onClick ?: { dialogVisible = true },
            isClearable = isClearable,
            onClear = {
                onValueChange(null)
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLayoutIcon(displayMode: MutableState<DisplayMode>) {
    IconButton(
        onClick = {
            displayMode.value = when (displayMode.value) {
                DisplayMode.Input -> DisplayMode.Picker
                else -> DisplayMode.Input
            }
        }
    ) {
        Icon(
            imageVector = when (displayMode.value) {
                DisplayMode.Input -> Icons.Outlined.AccessTime
                else -> Icons.Outlined.KeyboardAlt
            },
            contentDescription = null,
            tint = LocalContentColor.current.copy(alpha = 0.8f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePicker(
    timePickerState: TimePickerState,
    modifier: Modifier = Modifier,
    displayMode: MutableState<DisplayMode>,
    title: String,
    changeLayoutIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Text(text = title)
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedContent(
            targetState = displayMode.value,
            label = "TimePicker"
        ) { targetState ->
            when (targetState) {
                DisplayMode.Picker -> {
                    TimePicker(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors(),
                        layoutType = TimePickerLayoutType.Vertical
                    )
                }

                DisplayMode.Input -> {
                    TimeInput(
                        state = timePickerState,
                        colors = TimePickerDefaults.colors()
                    )
                }
            }
        }
        changeLayoutIcon?.invoke()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    displayModeIcon: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.wrapContentHeight()
    ) {
        Surface(shape = MaterialTheme.shapes.large) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(24.dp)
            ) {
                content()
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    displayModeIcon?.invoke()
                    Spacer(modifier = modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}
