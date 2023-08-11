package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import com.google.android.material.timepicker.TimeFormat
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.field.base.TextFieldInputMode
import com.icell.external.carlosformito.ui.theme.LocalCarlosIcons
import com.icell.external.carlosformito.ui.util.TimePickerBuilder
import com.icell.external.carlosformito.ui.util.extension.collectFieldState
import com.icell.external.carlosformito.ui.util.extension.errorMessage
import com.icell.external.carlosformito.ui.util.extension.requireActivity
import com.icell.external.carlosformito.ui.util.onFocusCleared
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun FormTimePickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<LocalTime>,
    label: String,
    timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm"),
    dialogTitle: String = label,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: (() -> Unit)? = null,
    timeFormat: Int = TimeFormat.CLOCK_24H,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null
) {
    val state by fieldItem.collectFieldState()
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
        dialogTitle = dialogTitle,
        leadingContentType = leadingContentType,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        enabled = enabled,
        isClearable = isClearable,
        onClick = onClick,
        timeFormat = timeFormat,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        contentDescription = contentDescription,
        supportingText = supportingText
    )
}

@Composable
fun FormTimePickerField(
    modifier: Modifier = Modifier,
    value: LocalTime?,
    label: String,
    timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm"),
    onValueChange: (LocalTime?) -> Unit,
    dialogTitle: String = label,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: (() -> Unit)? = null,
    onFocusCleared: () -> Unit = {},
    timeFormat: Int = TimeFormat.CLOCK_24H,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null
) {
    val context = LocalContext.current
    val carlosIcons = LocalCarlosIcons.current
    BaseTextField(
        modifier = modifier.onFocusCleared(onFocusCleared),
        value = value?.let { timeFormatter.format(value) } ?: "",
        label = label,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
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
        onValueChange = {
            // intentionally blank
        },
        inputMode = TextFieldInputMode.Picker(
            onClick = onClick ?: {
                TimePickerBuilder.build(
                    dialogTitle = dialogTitle,
                    selectedTime = value,
                    timeFormat = timeFormat,
                    onTimeSelected = { selectedDate ->
                        onValueChange(selectedDate)
                    }
                ).show(context.requireActivity().supportFragmentManager, "FormTimePickerField")
            },
            isClearable = isClearable,
            onClear = {
                onValueChange(null)
            }
        )
    )
}
