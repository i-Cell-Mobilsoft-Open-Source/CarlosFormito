package com.icell.external.carlosformito.ui.field

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.api.FormFieldItem
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.field.base.TextFieldInputMode
import com.icell.external.carlosformito.ui.theme.LocalCarlosIcons
import com.icell.external.carlosformito.ui.util.extension.collectFieldState
import com.icell.external.carlosformito.ui.util.extension.errorMessage
import com.icell.external.carlosformito.ui.util.onFocusCleared

@Composable
fun <T> FormPickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<T>,
    label: String,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    onClick: () -> Unit,
    displayedValue: (T?) -> String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val state by fieldItem.collectFieldState()
    FormPickerField(
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
        contentDescription = contentDescription,
        supportingText = supportingText,
        testTag = testTag
    )
}

@Composable
fun <T> FormPickerField(
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
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val carlosIcons = LocalCarlosIcons.current
    BaseTextField(
        modifier = modifier.onFocusCleared(onFocusCleared),
        value = displayedValue(value),
        label = label,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
        trailingContentType = TextFieldAffixContentType.Icon(
            value = if (isClearable && value != null) {
                carlosIcons.clear
            } else {
                carlosIcons.arrowDropDown
            }
        ),
        leadingContentType = leadingContentType,
        contentDescription = contentDescription,
        visualTransformation = visualTransformation,
        supportingText = supportingText,
        testTag = testTag,
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
