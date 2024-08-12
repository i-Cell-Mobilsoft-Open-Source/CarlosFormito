package com.icell.external.carlosformito.ui.field

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.ui.collectFieldState
import com.icell.external.carlosformito.core.ui.errorMessage
import com.icell.external.carlosformito.ui.field.base.BasePickerField
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs

/**
 * A composable function for a picker field with various customization options.
 *
 * @param modifier a [Modifier] for this picker field
 * @param fieldItem the [FormFieldItem] that holds the state of the form field and dispatches ui related events
 * e.g. field value change, visibility change and focus clear to the form manager
 * @param textStyle the style to be applied to the input text. The default [textStyle] uses the
 * [LocalCarlosConfigs]'s textStyle defined by the theme.
 * @param label the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [Typography.bodySmall] when the text field is in focus and
 * [Typography.bodyLarge] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [Typography.bodyLarge]
 * @param leadingIcon the optional leading icon to be displayed at the beginning of the picker field
 * container
 * @param pickerIcon the [ImageVector] icon displayed at the end of the picker field
 * when the current value is null or [isClearable] is `false`
 * @param prefix the optional prefix to be displayed before the displayed value in the picker field
 * @param suffix the optional suffix to be displayed after the displayed value in the picker field
 * @param outlined boolean indicating if the picker field should be outlined.
 * The default [outlined] uses the [LocalCarlosConfigs]'s outlined param defined by the theme.
 * @param shape the shape of the picker field's border
 * The default [shape] uses the [LocalCarlosConfigs]'s shape defined by the theme, which defaults to
 * [OutlinedTextFieldDefaults.shape] if the field is outlined or [TextFieldDefaults.shape] otherwise.
 * @param colors [TextFieldColors] that will be used to resolve color of the text and content
 * (including label, placeholder, leading and trailing icons, border) for this picker field in
 * different states. The default [colors] uses the [LocalCarlosConfigs]'s colors defined by the theme, which defaults to
 * [OutlinedTextFieldDefaults.colors] if the field is outlined or [TextFieldDefaults.colors] otherwise
 * @param onClick the callback that is triggered when the picker field is clicked
 * @param displayedValue a lambda function that converts the current value of type [T] to a string for display
 * @param enabled controls the enabled state of the picker field. When `false`, the picker field will
 * be neither editable nor focusable, the input of the picker field will not be selectable,
 * visually picker field will appear in the disabled UI state
 * @param isClearable indicates if the picked value of the picker field can be cleared
 * @param clearIcon the [ImageVector] icon displayed at the end of the picker field
 * when the current value is not null and [isClearable] is `true`
 * @param contentDescription optional content description for accessibility
 * @param customErrorMessage a custom error message to be displayed when there is an error.
 * If null, the default error message from the field's state is used
 * @param supportingText the optional supporting text to be displayed below the picker field
 * @param testTag optional test tag for testing purposes
 */
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
