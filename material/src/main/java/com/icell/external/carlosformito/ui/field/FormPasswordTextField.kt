package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs

/**
 * A composable function for a password text field with various customization options.
 *
 * @param modifier a [Modifier] for this text field
 * @param maxLength maximum number of characters allowed in the text field. If null, there is no limit.
 * @param fieldItem the [FormFieldItem] that holds the state of the form field and dispatches ui related events
 * e.g. field value change, visibility change and focus clear to the form manager
 * @param textStyle the style to be applied to the input text. The default [textStyle] uses the
 * [LocalCarlosConfigs]'s textStyle defined by the theme.
 * @param label the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [Typography.caption] when the text field is in focus and
 * [Typography.subtitle1] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [Typography.subtitle1]
 * @param leadingIcon the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param passwordVisibleIcon the [ImageVector] icon displayed when the password is visible.
 * @param passwordInvisibleIcon the [ImageVector] icon displayed when the password is hidden.
 * @param enabled controls the enabled state of the text field. When `false`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state
 * @param readOnly controls the editable state of the text field. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction]
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction]
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this text field. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this OutlinedTextField in different [Interaction]s.
 * @param outlined indicates if the text field should be outlined.
 * The default [outlined] uses the [LocalCarlosConfigs]'s outlined param defined by the theme.
 * @param shape the shape of the text field's border
 * The default [shape] uses the [LocalCarlosConfigs]'s shape defined by the theme, which defaults to
 * [TextFieldDefaults.OutlinedTextFieldShape] if the field is outlined or [TextFieldDefaults.TextFieldShape] otherwise
 * @param colors [TextFieldColors] that will be used to resolve color of the text and content
 * (including label, placeholder, leading and trailing icons, border) for this text field in
 * different states. The default [colors] uses the [LocalCarlosConfigs]'s colors defined by the theme, which defaults to
 * [TextFieldDefaults.outlinedTextFieldColors] if the field is outlined or [TextFieldDefaults.textFieldColors] otherwise
 * @param contentDescription optional content description for accessibility
 * @param customErrorMessage a custom error message to be displayed when there is an error.
 * If null, the default error message from the field's state is used.
 * @param supportingText the optional supporting text to be displayed below the text field
 * @param testTag optional test tag for testing purposes
 */
@Composable
fun FormPasswordTextField(
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    fieldItem: FormFieldItem<String>,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    passwordVisibleIcon: ImageVector = Icons.Default.Visibility,
    passwordInvisibleIcon: ImageVector = Icons.Default.VisibilityOff,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    outlined: Boolean = LocalCarlosConfigs.current.outlined,
    shape: Shape = LocalCarlosConfigs.current.shape,
    colors: TextFieldColors = LocalCarlosConfigs.current.colors,
    contentDescription: String? = null,
    customErrorMessage: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val isPasswordVisible = rememberSaveable { mutableStateOf(false) }
    val state by fieldItem.collectFieldState()
    val isError = state.isError || !customErrorMessage.isNullOrBlank()

    BaseTextField(
        modifier = modifier,
        value = state.value ?: "",
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = if (isPasswordVisible.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Password),
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        outlined = outlined,
        shape = shape,
        colors = colors,
        trailingIcon = {
            Icon(
                imageVector = if (isPasswordVisible.value) {
                    passwordVisibleIcon
                } else {
                    passwordInvisibleIcon
                },
                contentDescription = "",
                modifier = Modifier
                    .clickable {
                        isPasswordVisible.value = !isPasswordVisible.value
                    }
            )
        },
        isError = isError,
        contentDescription = contentDescription,
        supportingText = if (isError) {
            customErrorMessage ?: state.errorMessage() ?: supportingText
        } else {
            supportingText
        },
        testTag = testTag,
        onValueChange = { newValue ->
            if (maxLength != null && newValue.length > maxLength) {
                return@BaseTextField
            }
            fieldItem.onFieldValueChanged(newValue)
        },
        onVisibilityChange = { visible ->
            fieldItem.onFieldVisibilityChanged(visible)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        }
    )
}
