package com.icell.external.carlosformito.ui.field.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs
import com.icell.external.carlosformito.ui.util.focusStepper
import com.icell.external.carlosformito.ui.util.onFocusCleared
import com.icell.external.carlosformito.ui.util.testId

@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onVisibilityChange: (visible: Boolean) -> Unit,
    onFocusCleared: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
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
    textSelectionColors: TextSelectionColors = LocalCarlosConfigs.current.textSelectionColors(isError),
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null
) {
    TrackVisibilityEffect(onVisibilityChange)

    val semanticsModifier = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
        }
    } else {
        Modifier
    }

    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 82.dp)
            .focusStepper()
            .onFocusCleared(onFocusCleared)
            .then(
                if (testTag != null) {
                    Modifier.testId("textField_$testTag")
                } else {
                    Modifier
                }
            )
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
            val textFieldModifier = Modifier
                .testId("textField")
                .fillMaxWidth()
                .then(semanticsModifier)

            if (outlined) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = textFieldModifier,
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = textStyle,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    isError = isError,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    shape = shape,
                    colors = colors
                )
            } else {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = textFieldModifier,
                    enabled = enabled,
                    readOnly = readOnly,
                    textStyle = textStyle,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    isError = isError,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    shape = shape,
                    colors = colors
                )
            }
        }
        AnimatedVisibility(visible = !supportingText.isNullOrBlank()) {
            TextFieldSupportingText(
                modifier = Modifier
                    .testId("text_supported")
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
                    .padding(horizontal = 16.dp),
                isError = isError,
                supportingText = supportingText ?: ""
            )
        }
    }
}
