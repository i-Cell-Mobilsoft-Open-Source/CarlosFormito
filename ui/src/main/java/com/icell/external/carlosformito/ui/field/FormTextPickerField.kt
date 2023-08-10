package com.icell.external.carlosformito.ui.field

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.R
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.field.base.TextFieldInputMode
import com.icell.external.carlosformito.ui.util.extension.collectFieldState
import com.icell.external.carlosformito.ui.util.extension.errorMessage
import com.icell.external.carlosformito.ui.util.onFocusCleared

@Composable
fun <T> FormTextPickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<T>,
    label: String,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: () -> Unit,
    displayedValue: (T?) -> String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null
) {
    val state by fieldItem.collectFieldState()
    FormTextPickerField(
        modifier = modifier,
        value = state.value,
        label = label,
        onClear = {
            fieldItem.onFieldValueChanged(null)
        },
        leadingContentType = leadingContentType,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        enabled = enabled,
        isClearable = isClearable,
        onClick = onClick,
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        },
        displayedValue = displayedValue,
        visualTransformation = visualTransformation,
        colors = colors,
        contentDescription = contentDescription,
        supportingText = supportingText
    )
}

@Composable
fun <T> FormTextPickerField(
    modifier: Modifier = Modifier,
    value: T?,
    label: String,
    onClear: () -> Unit = {},
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    errorMessage: CharSequence? = null,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: (() -> Unit),
    onFocusCleared: () -> Unit = {},
    displayedValue: (T?) -> String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null
) {
    BaseTextField(
        modifier = modifier.onFocusCleared(onFocusCleared),
        value = displayedValue(value),
        label = label,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
        colors = colors,
        trailingContentType = TextFieldAffixContentType.Icon(
            value = if (isClearable && value != null) {
                R.drawable.ic_close_simple
            } else {
                R.drawable.ic_arrow_drop_down
            }
        ),
        leadingContentType = leadingContentType,
        contentDescription = contentDescription,
        visualTransformation = visualTransformation,
        supportingText = supportingText,
        onValueChange = {
            // intentionally blank
        },
        inputMode = TextFieldInputMode.Picker(
            onClick = onClick,
            isClearable = isClearable,
            onClear = onClear
        )
    )
}
