package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.FormFieldHandle
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.util.extension.errorMessage
import com.icell.external.carlosformito.ui.util.focusStepper
import com.icell.external.carlosformito.ui.util.onFocusCleared

@Composable
fun FormIntegerField(
    modifier: Modifier = Modifier,
    state: FormFieldState<Int>,
    handle: FormFieldHandle<Int>,
    label: String,
    maxLength: Int? = null,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null
) {
    FormIntegerField(
        modifier = modifier,
        value = state.value,
        label = label,
        maxLength = maxLength,
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
        onValueChange = { newValue ->
            handle.onFieldValueChanged(newValue)
        },
        onFocusCleared = {
            handle.onFieldFocusCleared()
        }
    )
}

@Composable
fun FormIntegerField(
    modifier: Modifier = Modifier,
    state: FormFieldState<Int>,
    label: String,
    maxLength: Int? = null,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    onValueChange: (Int?) -> Unit,
    onFocusCleared: () -> Unit = {}
) {
    FormIntegerField(
        modifier = modifier,
        value = state.value,
        label = label,
        maxLength = maxLength,
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
        onFocusCleared = onFocusCleared
    )
}

@Composable
fun FormIntegerField(
    modifier: Modifier = Modifier,
    value: Int?,
    label: String,
    maxLength: Int? = null,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    onValueChange: (Int?) -> Unit,
    onFocusCleared: () -> Unit = {}
) {
    BaseTextField(
        modifier = modifier
            .focusStepper()
            .onFocusCleared(onFocusCleared),
        value = value?.toString() ?: "",
        label = label,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
        colors = colors,
        trailingContentType = trailingContentType,
        leadingContentType = leadingContentType,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions,
        contentDescription = contentDescription,
        visualTransformation = visualTransformation,
        supportingText = supportingText,
        onValueChange = { newValue ->
            if (newValue.isBlank()) {
                onValueChange(null)
            } else if (maxLength != null && newValue.length > maxLength) {
                return@BaseTextField
            } else {
                newValue.trim().toIntOrNull()?.let { integerValue ->
                    onValueChange(integerValue)
                }
            }
        }
    )
}
