package com.icell.external.carlosformito.ui.field.base

import android.view.KeyEvent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.icell.external.carlosformito.commonui.base.TextFieldAffixContentType
import com.icell.external.carlosformito.commonui.base.TextFieldInputMode
import com.icell.external.carlosformito.commonui.util.testId
import com.icell.external.carlosformito.ui.theme.LocalCarlosColors

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
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
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
        onValueChange = { fieldValue ->
            textFieldValueState = fieldValue
            if (value != fieldValue.text) {
                onValueChange(fieldValue.text)
            }
        },
        contentDescription = contentDescription,
        modifier = modifier.defaultMinSize(minHeight = 82.dp),
        supportingText = supportingText,
        testTag = testTag,
        inputMode = inputMode
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BaseTextField(
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
    onValueChange: (TextFieldValue) -> Unit,
    contentDescription: String? = null,
    supportingText: CharSequence? = null,
    testTag: String? = null,
    inputMode: TextFieldInputMode = TextFieldInputMode.Default
) {
    val carlosColors = LocalCarlosColors.current
    val textFieldColors = carlosColors.textFieldColors
    val textSelectionColors = carlosColors.textSelectionColors(isError)

    val testIdModifier = if (testTag != null) {
        Modifier.testId(testTag)
    } else {
        Modifier
    }

    val semanticsModifier = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
        }
    } else {
        Modifier
    }

    val textFieldFocusRequester = remember { FocusRequester() }

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
            .clickable(
                enabled = enabled,
                onClick = {
                    textFieldFocusRequester.requestFocus()
                    inputMode.onClick()
                }
            )
    } else {
        Modifier
    }

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
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
            prefix = {
                if (leadingContentType is TextFieldAffixContentType.Text) {
                    Text(text = leadingContentType.value)
                }
            },
            leadingIcon = {
                if (leadingContentType is TextFieldAffixContentType.Icon) {
                    Icon(
                        painterResource(id = leadingContentType.value),
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { leadingContentType.onClick.invoke() }
                    )
                }
            },
            suffix = {
                if (trailingContentType is TextFieldAffixContentType.Text) {
                    Text(text = trailingContentType.value)
                }
            },
            trailingIcon = {
                if (trailingContentType is TextFieldAffixContentType.Icon) {
                    Icon(
                        painterResource(id = trailingContentType.value),
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                trailingContentType.onClick.invoke()

                                if (inputMode is TextFieldInputMode.Picker &&
                                    inputMode.isClearable && value.text.isNotBlank()
                                ) {
                                    inputMode.onClear()
                                }
                            }
                    )
                }
            },
            label = {
                Text(text = label)
            },
            supportingText = {
                TextFieldSupportingText(
                    modifier = Modifier.animateContentSize(),
                    isError = isError,
                    errorMessage = errorMessage,
                    supportingText = supportingText
                )
            },
            colors = textFieldColors,
            isError = isError,
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(textFieldFocusRequester)
                .then(semanticsModifier)
                .then(pickerInputModeModifier)
                .then(testIdModifier)
        )
    }
}
