package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.commonui.base.TextFieldAffixContentType
import com.icell.external.carlosformito.commonui.extension.collectFieldState
import com.icell.external.carlosformito.commonui.extension.errorMessage
import com.icell.external.carlosformito.commonui.util.focusStepper
import com.icell.external.carlosformito.commonui.util.onFocusCleared
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.field.base.BaseTextField

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
        errorMessage = state.errorMessage(),
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        contentDescription = contentDescription,
        supportingText = supportingText,
        testTag = testTag,
        onValueChange = { newValue ->
            fieldItem.onFieldValueChanged(newValue)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        }
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
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
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
