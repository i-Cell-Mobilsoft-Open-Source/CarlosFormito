package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.field.base.TrackVisibilityEffect
import com.icell.external.carlosformito.ui.util.focusStepper
import com.icell.external.carlosformito.ui.util.onFocusCleared

@Composable
fun FormIntegerField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<Int>,
    label: String,
    maxLength: Int? = null,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val state by fieldItem.collectFieldState()
    FormIntegerField(
        modifier = modifier,
        value = state.value,
        label = label,
        maxLength = maxLength,
        trailingContentType = trailingContentType,
        leadingContentType = leadingContentType,
        isError = state.isError,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        contentDescription = contentDescription,
        supportingText = if (state.isError) {
            state.errorMessage() ?: supportingText
        } else {
            supportingText
        },
        testTag = testTag,
        onValueChange = { newValue ->
            fieldItem.onFieldValueChanged(newValue)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        },
        onVisibilityChanged = { visible ->
            fieldItem.onFieldVisibilityChanged(visible)
        }
    )
}

@Composable
private fun FormIntegerField(
    modifier: Modifier = Modifier,
    value: Int?,
    label: String,
    maxLength: Int? = null,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
    onValueChange: (Int?) -> Unit,
    onFocusCleared: () -> Unit = {},
    onVisibilityChanged: (visible: Boolean) -> Unit = {},
) {
    TrackVisibilityEffect(onVisibilityChanged)
    BaseTextField(
        modifier = modifier
            .focusStepper()
            .onFocusCleared(onFocusCleared),
        value = value?.toString() ?: "",
        label = label,
        enabled = enabled,
        isError = isError,
        trailingContentType = trailingContentType,
        leadingContentType = leadingContentType,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions,
        contentDescription = contentDescription,
        visualTransformation = visualTransformation,
        supportingText = supportingText,
        testTag = testTag,
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
