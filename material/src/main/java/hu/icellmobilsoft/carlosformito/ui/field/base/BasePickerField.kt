package hu.icellmobilsoft.carlosformito.ui.field.base

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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.Typography
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
import hu.icellmobilsoft.carlosformito.core.ui.TrackVisibilityEffect
import hu.icellmobilsoft.carlosformito.core.ui.focusStepper
import hu.icellmobilsoft.carlosformito.core.ui.onFocusCleared
import hu.icellmobilsoft.carlosformito.core.ui.testId
import hu.icellmobilsoft.carlosformito.ui.theme.LocalCarlosConfigs

/**
 * A base composable function for a picker field with various customization options.
 *
 * @param value the input text to be shown in the picker field
 * @param onClick the callback that is triggered when the picker field is clicked
 * @param onVisibilityChange the callback that is triggered when the picker field visibility changes.
 * It is called with visible = `true` when the picker field is composed for the first time
 * And called with `false` when the picker field leaves composition (onDispose)
 * @param onFocusCleared the callback that is triggered when the picker field looses focus
 * @param modifier a [Modifier] for this picker field
 * @param onClear the callback that is triggered when the picker field's clear button is clicked
 * @param isClearable indicates if the picked value of the picker field can be cleared
 * @param enabled controls the enabled state of the picker field. When `false`, the picker field will
 * be neither editable nor focusable, the input of the picker field will not be selectable,
 * visually picker field will appear in the disabled UI state
 * @param textStyle the style to be applied to the input text. The default [textStyle] uses the
 * [LocalCarlosConfigs]'s textStyle defined by the theme.
 * @param label the optional label to be displayed inside the picker field container. The default
 * text style for internal [Text] is [Typography.caption] when the picker field is in focus and
 * [Typography.subtitle1] when the picker field is not in focus
 * @param placeholder the optional placeholder to be displayed when the picker field is in focus and
 * the input text is empty. The default text style for internal [Text] is [Typography.subtitle1]
 * @param leadingIcon the optional leading icon to be displayed at the beginning of the picker field
 * container
 * @param trailingIcon the optional trailing icon to be displayed at the end of the picker field
 * container
 * @param isError indicates if the picker field's current value is in error. If set to true, the
 * label, bottom indicator and trailing icon by default will be displayed in error color
 * @param outlined boolean indicating if the picker field should be outlined.
 * The default [outlined] uses the [LocalCarlosConfigs]'s outlined param defined by the theme.
 * @param shape the shape of the picker field's border
 * The default [shape] uses the [LocalCarlosConfigs]'s shape defined by the theme, which defaults to
 * [TextFieldDefaults.OutlinedTextFieldShape] if the field is outlined or [TextFieldDefaults.TextFieldShape] otherwise.
 * @param colors [TextFieldColors] that will be used to resolve color of the text and content
 * (including label, placeholder, leading and trailing icons, border) for this picker field in
 * different states. The default [colors] uses the [LocalCarlosConfigs]'s colors defined by the theme, which defaults to
 * [TextFieldDefaults.outlinedTextFieldColors] if the field is outlined or [TextFieldDefaults.textFieldColors] otherwise
 * @param textSelectionColors the colors to be used for text selection
 * The default [textSelectionColors] uses the [LocalCarlosConfigs]'s text selection colors defined by the theme.
 * @param contentDescription optional content description for accessibility
 * @param supportingText the optional supporting text to be displayed below the picker field
 * @param testTag optional test tag for testing purposes
 */
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
