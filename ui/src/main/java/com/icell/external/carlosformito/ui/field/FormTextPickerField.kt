package com.icell.external.carlosformito.ui.field

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
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
fun FormTextPickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<String>,
    label: String,
    onClear: () -> Unit = {},
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: () -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: AnnotatedString? = null
) {
    val state by fieldItem.collectFieldState()
    FormTextPickerField(
        modifier = modifier,
        value = state.value,
        label = label,
        onClear = onClear,
        leadingContentType = leadingContentType,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        enabled = enabled,
        isClearable = isClearable,
        onClick = onClick,
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        },
        visualTransformation = visualTransformation,
        colors = colors,
        contentDescription = contentDescription,
        supportingText = supportingText
    )
}

@Composable
fun FormTextPickerField(
    modifier: Modifier = Modifier,
    value: String?,
    label: String,
    onClear: () -> Unit = {},
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: (() -> Unit),
    onFocusCleared: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: AnnotatedString? = null
) {
    BaseTextField(
        modifier = modifier.onFocusCleared(onFocusCleared),
        value = value ?: "",
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
