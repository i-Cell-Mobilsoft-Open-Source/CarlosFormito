package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.commonui.base.TextFieldAffixContentType
import com.icell.external.carlosformito.commonui.extension.collectFieldState
import com.icell.external.carlosformito.commonui.extension.errorMessage
import com.icell.external.carlosformito.commonui.util.focusStepper
import com.icell.external.carlosformito.commonui.util.onFocusCleared
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.field.base.BaseTextField

@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    fieldItem: FormFieldItem<String>,
    label: String,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
    inputPattern: String? = null,
    textChanger: ((String) -> String)? = null
) {
    val state by fieldItem.collectFieldState()
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
        contentDescription = contentDescription,
        supportingText = supportingText,
        testTag = testTag,
        onValueChange = { value ->
            fieldItem.onFieldValueChanged(value)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        },
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
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
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
        trailingContentType = trailingContentType,
        leadingContentType = leadingContentType,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        contentDescription = contentDescription,
        visualTransformation = visualTransformation,
        supportingText = supportingText,
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
            onValueChange(spaceNormalizedValue)
        }
    )
}
