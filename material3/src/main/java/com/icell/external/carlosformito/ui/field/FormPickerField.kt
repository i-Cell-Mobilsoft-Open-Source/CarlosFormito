package com.icell.external.carlosformito.ui.field

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.BasePickerField
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs

@Composable
fun <T> FormPickerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<T>,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    pickerIcon: ImageVector = Icons.Default.Edit,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    outlined: Boolean = LocalCarlosConfigs.current.outlined,
    shape: Shape = LocalCarlosConfigs.current.shape,
    colors: TextFieldColors = LocalCarlosConfigs.current.colors,
    onClick: () -> Unit,
    displayedValue: (T?) -> String,
    enabled: Boolean = true,
    isClearable: Boolean = true,
    clearIcon: ImageVector = Icons.Default.Clear,
    contentDescription: String? = null,
    customErrorMessage: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
) {
    val state by fieldItem.collectFieldState()
    val isError = state.isError || !customErrorMessage.isNullOrBlank()

    BasePickerField(
        modifier = modifier,
        value = displayedValue(state.value),
        onClick = onClick,
        isClearable = isClearable,
        onClear = {
            fieldItem.onFieldValueChanged(null)
        },
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            Icon(
                imageVector = if (!isClearable || state.value == null) {
                    pickerIcon
                } else {
                    clearIcon
                },
                contentDescription = ""
            )
        },
        prefix = prefix,
        suffix = suffix,
        outlined = outlined,
        shape = shape,
        colors = colors,
        enabled = enabled,
        isError = isError,
        contentDescription = contentDescription,
        supportingText = if (isError) {
            customErrorMessage ?: state.errorMessage() ?: supportingText
        } else {
            supportingText
        },
        testTag = testTag,
        onVisibilityChange = { visible ->
            fieldItem.onFieldVisibilityChanged(visible)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        }
    )
}
