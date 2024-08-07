package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs

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
