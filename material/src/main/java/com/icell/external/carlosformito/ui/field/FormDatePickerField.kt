package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.field.base.TextFieldInputMode
import com.icell.external.carlosformito.ui.theme.LocalCarlosFormats
import com.icell.external.carlosformito.ui.theme.LocalCarlosIcons
import com.icell.external.carlosformito.ui.util.DatePickerBuilder
import com.icell.external.carlosformito.ui.util.extension.collectFieldState
import com.icell.external.carlosformito.ui.util.extension.errorMessage
import com.icell.external.carlosformito.ui.util.extension.requireActivity
import com.icell.external.carlosformito.ui.util.onFocusCleared
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FormDatePickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<LocalDate>,
    label: String,
    dateFormatter: DateTimeFormatter = LocalCarlosFormats.current.dateFormatter,
    dialogTitle: String = label,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val state by fieldItem.collectFieldState()
    FormDatePickerField(
        modifier = modifier,
        value = state.value,
        label = label,
        dateFormatter = dateFormatter,
        onValueChange = { value ->
            fieldItem.onFieldValueChanged(value)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        },
        dialogTitle = dialogTitle,
        minDate = minDate,
        maxDate = maxDate,
        leadingContentType = leadingContentType,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        enabled = enabled,
        isClearable = isClearable,
        onClick = onClick,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        contentDescription = contentDescription,
        supportingText = supportingText,
        testTag = testTag
    )
}

@Composable
fun FormDatePickerField(
    modifier: Modifier = Modifier,
    value: LocalDate?,
    label: String,
    dateFormatter: DateTimeFormatter = LocalCarlosFormats.current.dateFormatter,
    onValueChange: (LocalDate?) -> Unit,
    dialogTitle: String = label,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: (() -> Unit)? = null,
    onFocusCleared: () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val context = LocalContext.current
    val carlosIcons = LocalCarlosIcons.current
    BaseTextField(
        modifier = modifier.onFocusCleared(onFocusCleared),
        value = value?.let { dateFormatter.format(value) } ?: "",
        label = label,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
        trailingContentType = TextFieldAffixContentType.Icon(
            value = if (isClearable && value != null) {
                carlosIcons.clear
            } else {
                carlosIcons.calendar
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
            onClick = onClick ?: {
                DatePickerBuilder.build(
                    dialogTitle = dialogTitle,
                    minDate = minDate,
                    maxDate = maxDate,
                    selectedDate = value,
                    onDateSelected = { selectedDate ->
                        onValueChange(selectedDate)
                    }
                ).show(context.requireActivity().supportFragmentManager, "FormDatePickerField")
            },
            isClearable = isClearable,
            onClear = {
                onValueChange(null)
            }
        )
    )
}
