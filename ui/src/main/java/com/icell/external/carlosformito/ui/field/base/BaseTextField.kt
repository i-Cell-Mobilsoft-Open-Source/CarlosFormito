package com.icell.external.carlosformito.ui.field.base

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: CharSequence? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    onValueChange: (String) -> Unit,
    inputMode: TextFieldInputMode = TextFieldInputMode.Default
) {
    var textFieldValueState by remember {
        mutableStateOf(TextFieldValue(text = value))
    }
    val textFieldValue = textFieldValueState.copy(text = value)
    BaseTextField(
        value = textFieldValue,
        label = label,
        trailingContentType = trailingContentType,
        leadingContentType = leadingContentType,
        visualTransformation = visualTransformation,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors,
        onValueChange = { fieldValue ->
            textFieldValueState = fieldValue
            if (value != fieldValue.text) {
                onValueChange(fieldValue.text)
            }
        },
        contentDescription = contentDescription,
        modifier = modifier.defaultMinSize(minHeight = 82.dp),
        supportingText = supportingText,
        inputMode = inputMode
    )
}

@Suppress("CyclomaticComplexMethod")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BaseTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    label: String,
    trailingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    leadingContentType: TextFieldAffixContentType = TextFieldAffixContentType.None,
    isError: Boolean = false,
    errorMessage: CharSequence? = null,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    onValueChange: (TextFieldValue) -> Unit,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    inputMode: TextFieldInputMode = TextFieldInputMode.Default
) {
    val semanticsModifier = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
        }
    } else {
        Modifier
    }

    val pickerInputModeModifier = if (inputMode is TextFieldInputMode.Picker) {
        Modifier.onPreviewKeyEvent { keyEvent ->
            if (keyEvent.key == Key.Enter && keyEvent.nativeKeyEvent.action == ACTION_DOWN) {
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

    BaseFieldFrame(
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage,
        supportingText = supportingText
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.body1,
            maxLines = 1,
            singleLine = true,
            enabled = enabled,
            readOnly = inputMode is TextFieldInputMode.Picker,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            leadingIcon = when (leadingContentType) {
                TextFieldAffixContentType.None -> null
                is TextFieldAffixContentType.Icon -> {
                    {
                        Icon(
                            painterResource(id = leadingContentType.value),
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { leadingContentType.onClick.invoke() }
                        )
                    }
                }

                is TextFieldAffixContentType.Text -> {
                    {
                        Text(
                            color = colors.textColor(enabled).value,
                            text = leadingContentType.value,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            },
            trailingIcon = when (trailingContentType) {
                TextFieldAffixContentType.None -> null
                is TextFieldAffixContentType.Icon -> {
                    {
                        Icon(
                            painterResource(id = trailingContentType.value),
                            contentDescription = "",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { trailingContentType.onClick.invoke() }
                        )
                    }
                }

                is TextFieldAffixContentType.Text -> {
                    {
                        Text(
                            color = colors.textColor(enabled).value,
                            text = trailingContentType.value,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            },
            label = {
                Text(
                    text = label
                )
            },
            colors = colors,
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(textFieldFocusRequester)
                .then(semanticsModifier)
                .then(pickerInputModeModifier)
        )
        if (inputMode is TextFieldInputMode.Picker) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        enabled = enabled,
                        onClick = {
                            textFieldFocusRequester.requestFocus()
                            inputMode.onClick()
                        }
                    )
            )
            if (inputMode.isClearable && value.text.isNotBlank()) {
                Box(
                    modifier = Modifier
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
