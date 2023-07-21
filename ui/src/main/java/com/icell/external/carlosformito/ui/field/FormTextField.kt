package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.FormFieldHandle
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.util.extension.errorMessage
import com.icell.external.carlosformito.ui.util.focusStepper
import com.icell.external.carlosformito.ui.util.onFocusCleared

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    state: FormFieldState<String>,
    handle: FormFieldHandle<String>,
    label: String,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    inputPattern: String? = null,
    textChanger: ((String) -> String)? = null
) {
    FormTextField(
        modifier = modifier,
        maxLength = maxLength,
        value = state.value,
        label = label,
        trailingContentType = trailingContentType,
        leadingContentType = leadingContentType,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        colors = colors,
        contentDescription = contentDescription,
        supportingText = supportingText,
        onValueChange = { value ->
            handle.onFieldValueChanged(value)
        },
        onFocusCleared = {
            handle.onFieldFocusCleared()
        },
        inputPattern = inputPattern,
        textChanger = textChanger
    )
}

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    state: FormFieldState<String>,
    label: String,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    onValueChange: (String) -> Unit,
    onFocusCleared: () -> Unit = {},
    inputPattern: String? = null,
    textChanger: ((String) -> String)? = null
) {
    FormTextField(
        modifier = modifier,
        maxLength = maxLength,
        value = state.value,
        label = label,
        trailingContentType = trailingContentType,
        leadingContentType = leadingContentType,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        colors = colors,
        contentDescription = contentDescription,
        supportingText = supportingText,
        onValueChange = onValueChange,
        onFocusCleared = onFocusCleared,
        inputPattern = inputPattern,
        textChanger = textChanger
    )
}

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    value: String?,
    label: String,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    errorMessage: CharSequence? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    onValueChange: (String) -> Unit,
    onFocusCleared: () -> Unit = {},
    inputPattern: String? = null,
    textChanger: ((String) -> String)? = null
) {
    BaseTextField(
        modifier = modifier
            .focusStepper()
            .onFocusCleared(onFocusCleared),
        value = value ?: "",
        label = label,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
        colors = colors,
        trailingContentType = trailingContentType,
        leadingContentType = leadingContentType,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        contentDescription = contentDescription,
        visualTransformation = visualTransformation,
        supportingText = supportingText,
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
            onValueChange(spaceNormalizedValue)
        }
    )
}
