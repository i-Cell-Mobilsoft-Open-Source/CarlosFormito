package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.commonui.base.TextFieldAffixContentType
import com.icell.external.carlosformito.commonui.base.TextFieldInputMode
import com.icell.external.carlosformito.commonui.extension.collectFieldState
import com.icell.external.carlosformito.commonui.extension.errorMessage
import com.icell.external.carlosformito.commonui.util.onFocusCleared
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.extension.isAfterOrEqual
import com.icell.external.carlosformito.ui.field.extension.isBeforeOrEqual
import com.icell.external.carlosformito.ui.field.extension.selectedDate
import com.icell.external.carlosformito.ui.field.extension.toEpochMillis
import com.icell.external.carlosformito.ui.field.extension.toLocalDate
import com.icell.external.carlosformito.ui.theme.LocalCarlosFormats
import com.icell.external.carlosformito.ui.theme.LocalCarlosIcons
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDatePickerField(
    modifier: Modifier = Modifier,
    value: LocalDate?,
    label: String,
    dateFormatter: DateTimeFormatter = LocalCarlosFormats.current.dateFormatter,
    onValueChange: (LocalDate?) -> Unit,
    dialogTitle: String = label,
    dialogHeadline: String? = null,
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

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value?.toEpochMillis(),
        initialDisplayMode = DisplayMode.Picker
    )

    var dialogVisible by remember { mutableStateOf(false) }
    if (dialogVisible) {
        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = { dialogVisible = false },
            confirmButton = {
                TextButton(
                    onClick = { dialogVisible = false },
                    enabled = confirmEnabled
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
            }
        ) {
            DatePicker(
                datePickerState = datePickerState,
                minDate = minDate,
                maxDate = maxDate,
                title = dialogTitle,
                headline = dialogHeadline ?: "",
                formatter = dateFormatter
            )
        }
    }

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
private fun DatePicker(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    title: String,
    headline: String,
    minDate: LocalDate?,
    maxDate: LocalDate?,
    formatter: DateTimeFormatter
) {
    DatePicker(
        state = datePickerState,
        modifier = modifier,
        dateValidator = { selection ->
            val selectedDate = selection.toLocalDate()

            val minValid = minDate?.let { selectedDate.isAfterOrEqual(minDate) } ?: true
            val maxValid = maxDate?.let { selectedDate.isBeforeOrEqual(maxDate) } ?: true

            minValid && maxValid
        },
        title = {
            Text(
                text = title,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
            )
        },
        headline = {
            Text(
                text = datePickerState.selectedDate?.format(formatter) ?: headline,
                modifier = Modifier.padding(start = 24.dp)
            )
        },
        showModeToggle = true,
        colors = DatePickerDefaults.colors()
    )
}
