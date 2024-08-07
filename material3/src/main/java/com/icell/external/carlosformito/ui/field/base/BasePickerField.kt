package com.icell.external.carlosformito.ui.field.base

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs
import com.icell.external.carlosformito.ui.util.focusStepper
import com.icell.external.carlosformito.ui.util.onFocusCleared
import com.icell.external.carlosformito.ui.util.testId

@Composable
fun BasePickerField(
    value: String,
    onClick: () -> Unit,
    onVisibilityChange: (visible: Boolean) -> Unit,
    onFocusCleared: () -> Unit,
    modifier: Modifier = Modifier,
    onClear: () -> Unit = {},
    isClearable: Boolean = false,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalCarlosConfigs.current.textStyle,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
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

    /**
     * Only for testing purposes
     */
    val virtualKeyBoardInputModifier = Modifier.onPreviewKeyEvent { keyEvent ->
        if (keyEvent.key == Key.Enter && keyEvent.nativeKeyEvent.action == ACTION_DOWN) {
            onClick()
            true
        } else {
            false
        }
    }

    val textFieldFocusRequester = remember { FocusRequester() }

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
            Box {
                val textFieldModifier = Modifier
                    .testId("textField")
                    .fillMaxWidth()
                    .focusRequester(textFieldFocusRequester)
                    .then(semanticsModifier)
                    .then(virtualKeyBoardInputModifier)

                if (outlined) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {
                            // Intentionally blank
                        },
                        modifier = textFieldModifier,
                        enabled = enabled,
                        readOnly = true,
                        textStyle = textStyle,
                        label = label,
                        placeholder = placeholder,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        isError = isError,
                        shape = shape,
                        colors = colors
                    )
                } else {
                    TextField(
                        value = value,
                        onValueChange = {
                            // Intentionally blank
                        },
                        modifier = textFieldModifier,
                        enabled = enabled,
                        readOnly = true,
                        textStyle = textStyle,
                        label = label,
                        placeholder = placeholder,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        isError = isError,
                        shape = shape,
                        colors = colors
                    )
                }

                Box(
                    modifier = Modifier
                        .testId("picker")
                        .matchParentSize()
                        .clip(shape)
                        .clickable(
                            enabled = enabled,
                            onClick = {
                                textFieldFocusRequester.requestFocus()
                                onClick()
                            }
                        )
                )
                if (isClearable && value.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .testId("clear")
                            .size(48.dp)
                            .align(Alignment.CenterEnd)
                            .clickable(
                                enabled = enabled,
                                onClick = {
                                    onClear()
                                }
                            )
                    )
                }
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
