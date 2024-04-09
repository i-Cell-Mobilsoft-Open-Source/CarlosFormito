package com.icell.external.carlosformito.ui.field.base

import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.ui.theme.LocalCarlosColors
import com.icell.external.carlosformito.ui.util.testId

@Suppress("LongMethod")
@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
    inputMode: TextFieldInputMode = TextFieldInputMode.Default
) {
    val carlosColors = LocalCarlosColors.current
    val textFieldColors = carlosColors.textFieldColors
    val textSelectionColors = carlosColors.textSelectionColors(isError)

    val semanticsModifier = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
        }
    } else {
        Modifier
    }

    val pickerInputModeModifier = if (inputMode is TextFieldInputMode.Picker) {
        Modifier
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Enter && keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    inputMode.onClick()
                    true
                } else {
                    false
                }
            }
    } else {
        Modifier
    }

    val textFieldFocusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .defaultMinSize(minHeight = 82.dp)
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
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    maxLines = 1,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    enabled = enabled,
                    readOnly = inputMode is TextFieldInputMode.Picker,
                    visualTransformation = visualTransformation,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    prefix = if (leadingContentType is TextFieldAffixContentType.Text) {
                        {
                            Text(text = leadingContentType.value)
                        }
                    } else {
                        null
                    },
                    leadingIcon = if (leadingContentType is TextFieldAffixContentType.Icon) {
                        {
                            Icon(
                                painterResource(id = leadingContentType.value),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { leadingContentType.onClick.invoke() }
                            )
                        }
                    } else {
                        null
                    },
                    suffix = if (trailingContentType is TextFieldAffixContentType.Text) {
                        {
                            Text(text = trailingContentType.value)
                        }
                    } else {
                        null
                    },
                    trailingIcon = if (trailingContentType is TextFieldAffixContentType.Icon) {
                        {
                            Icon(
                                painterResource(id = trailingContentType.value),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { trailingContentType.onClick.invoke() }
                            )
                        }
                    } else {
                        null
                    },
                    label = {
                        Text(text = label)
                    },
                    colors = textFieldColors,
                    isError = isError,
                    modifier = Modifier
                        .testId("textField")
                        .fillMaxWidth()
                        .focusRequester(textFieldFocusRequester)
                        .then(semanticsModifier)
                        .then(pickerInputModeModifier)
                )
                if (inputMode is TextFieldInputMode.Picker) {
                    Box(
                        modifier = Modifier
                            .testId("picker")
                            .matchParentSize()
                            .clip(
                                MaterialTheme.shapes.small.copy(
                                    bottomEnd = ZeroCornerSize,
                                    bottomStart = ZeroCornerSize
                                )
                            )
                            .clickable(
                                enabled = enabled,
                                onClick = {
                                    textFieldFocusRequester.requestFocus()
                                    inputMode.onClick()
                                }
                            )
                    )
                    if (inputMode.isClearable && value.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .testId("clear")
                                .size(48.dp)
                                .align(Alignment.CenterEnd)
                                .clickable(
                                    enabled = enabled,
                                    onClick = {
                                        inputMode.onClear()
                                    }
                                )
                        )
                    }
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
