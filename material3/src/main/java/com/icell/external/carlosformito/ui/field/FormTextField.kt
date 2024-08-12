package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.ui.collectFieldState
import com.icell.external.carlosformito.core.ui.errorMessage
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs

/**
 * A composable function for a text field with various customization options.
 *
 * @param modifier a [Modifier] for this text field
 * @param maxLength maximum number of characters allowed in the text field. If null, there is no limit.
 * @param fieldItem the [FormFieldItem] that holds the state of the form field and dispatches ui related events
 * e.g. field value change, visibility change and focus clear to the form manager
 * @param textStyle the style to be applied to the input text. The default [textStyle] uses the
 * [LocalCarlosConfigs]'s textStyle defined by the theme.
 * @param label the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [Typography.bodySmall] when the text field is in focus and
 * [Typography.bodyLarge] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [Typography.bodyLarge]
 * @param leadingIcon the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param trailingIcon the optional trailing icon to be displayed at the end of the text field
 * container
 * @param prefix the optional prefix to be displayed before the input text in the text field
 * @param suffix the optional suffix to be displayed after the input text in the text field
 * @param enabled controls the enabled state of the text field. When `false`, the text field will
 * be neither editable nor focusable, the input of the text field will not be selectable,
 * visually text field will appear in the disabled UI state
 * @param readOnly controls the editable state of the text field. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param visualTransformation transforms the visual representation of the input value
 * For example, you can use
 * [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation] to
 * create a password text field. By default no visual transformation is applied
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction]
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction]
 * @param singleLine when set to true, this text field becomes a single horizontally scrolling
 * text field instead of wrapping onto multiple lines. The keyboard will be informed to not show
 * the return key as the [ImeAction]. Note that [maxLines] parameter will be ignored as the
 * maxLines attribute will be automatically set to 1
 * @param maxLines the maximum height in terms of maximum number of visible lines. It is required
 * that 1 <= [minLines] <= [maxLines]. This parameter is ignored when [singleLine] is true.
 * @param minLines the minimum height in terms of minimum number of visible lines. It is required
 * that 1 <= [minLines] <= [maxLines]. This parameter is ignored when [singleLine] is true.
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this text field. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this OutlinedTextField in different [Interaction]s.
 * @param outlined indicates if the text field should be outlined.
 * The default [outlined] uses the [LocalCarlosConfigs]'s outlined param defined by the theme.
 * @param shape the shape of the text field's border
 * The default [shape] uses the [LocalCarlosConfigs]'s shape defined by the theme, which defaults to
 * [OutlinedTextFieldDefaults.shape] if the field is outlined or [TextFieldDefaults.shape] otherwise
 * @param colors [TextFieldColors] that will be used to resolve color of the text and content
 * (including label, placeholder, leading and trailing icons, border) for this text field in
 * different states. The default [colors] uses the [LocalCarlosConfigs]'s colors defined by the theme, which defaults to
 * [OutlinedTextFieldDefaults.colors] if the field is outlined or [TextFieldDefaults.colors] otherwise
 * @param contentDescription optional content description for accessibility
 * @param customErrorMessage a custom error message to be displayed when there is an error.
 * If null, the default error message from the field's state is used.
 * @param supportingText the optional supporting text to be displayed below the text field
 * @param testTag optional test tag for testing purposes
 * @param inputPattern a regex pattern that the input text must match.
 * The field's value will update only if the input text matches the pattern. If null, no pattern enforcement is applied.
 * @param textChanger a lambda that modifies the input text before it is set as the field's value.
 */
@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    fieldItem: FormFieldItem<String>,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    outlined: Boolean = LocalCarlosConfigs.current.outlined,
    shape: Shape = LocalCarlosConfigs.current.shape,
    colors: TextFieldColors = LocalCarlosConfigs.current.colors,
    contentDescription: String? = null,
    customErrorMessage: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
    inputPattern: String? = null,
    textChanger: ((String) -> String)? = null
) {
    val state by fieldItem.collectFieldState()
    val isError = state.isError || !customErrorMessage.isNullOrBlank()

    BaseTextField(
        modifier = modifier,
        value = state.value ?: "",
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        outlined = outlined,
        shape = shape,
        colors = colors,
        contentDescription = contentDescription,
        supportingText = if (isError) {
            customErrorMessage ?: state.errorMessage() ?: supportingText
        } else {
            supportingText
        },
        testTag = testTag,
        onValueChange = { newValue ->
            val changedText: String = textChanger?.invoke(newValue) ?: newValue
            if (maxLength != null && changedText.length > maxLength) {
                return@BaseTextField
            }
            inputPattern?.let { pattern ->
                if (!pattern.toRegex().containsMatchIn(changedText)) {
                    return@BaseTextField
                }
            }
            val spaceNormalizedValue: String = changedText.replace("  ", " ").trimStart()
            fieldItem.onFieldValueChanged(spaceNormalizedValue)
        },
        onVisibilityChange = { visible ->
            fieldItem.onFieldVisibilityChanged(visible)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        }
    )
}
