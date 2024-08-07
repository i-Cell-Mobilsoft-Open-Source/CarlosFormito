package com.icell.external.carlosformito.ui.field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.BaseTextField
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs

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
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
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
        prefix = prefix,
        suffix = suffix,
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
