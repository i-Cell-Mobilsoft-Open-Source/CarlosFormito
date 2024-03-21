package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.field.base.TextFieldAffixContentType
import com.icell.external.carlosformito.ui.theme.LocalCarlosIcons
import com.icell.external.carlosformito.ui.util.focusStepper
import com.icell.external.carlosformito.ui.util.onFocusCleared

@Composable
fun FormPasswordTextField(
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    fieldItem: FormFieldItem<String>,
    label: String,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    val state by fieldItem.collectFieldState()
    FormPasswordTextField(
        modifier = modifier,
        maxLength = maxLength,
        value = state.value,
        label = label,
        leadingContentType = leadingContentType,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        contentDescription = contentDescription,
        supportingText = supportingText,
        testTag = testTag,
        onValueChange = { value ->
            fieldItem.onFieldValueChanged(value)
        },
        onFocusCleared = {
            fieldItem.onFieldFocusCleared()
        }
    )
}

@Composable
private fun FormPasswordTextField(
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    value: String?,
    label: String,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
    onValueChange: (String) -> Unit,
    onFocusCleared: () -> Unit = {}
) {
    val carlosIcons = LocalCarlosIcons.current
    val isPasswordVisible = rememberSaveable { mutableStateOf(false) }

    BaseTextField(
        modifier = modifier
            .focusStepper()
            .onFocusCleared(onFocusCleared),
        value = value ?: "",
        label = label,
        enabled = enabled,
        isError = isError,
        errorMessage = errorMessage,
        trailingContentType = TextFieldAffixContentType.Icon(
            value = if (isPasswordVisible.value) {
                carlosIcons.passwordVisible
            } else {
                carlosIcons.passwordInvisible
            },
            onClick = {
                isPasswordVisible.value = !isPasswordVisible.value
            }
        ),
        leadingContentType = leadingContentType,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Password),
        keyboardActions = keyboardActions,
        contentDescription = contentDescription,
        visualTransformation = if (isPasswordVisible.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        supportingText = supportingText,
        testTag = testTag,
        onValueChange = { newValue ->
            if (maxLength != null && newValue.length > maxLength) {
                return@BaseTextField
            }
            onValueChange(newValue)
        }
    )
}
