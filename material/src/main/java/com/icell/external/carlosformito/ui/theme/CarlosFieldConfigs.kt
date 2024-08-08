package com.icell.external.carlosformito.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle

/**
 * A class that provides configuration options for Carlos form fields, including styles, shapes, colors,
 * and text selection behavior. It supports both outlined and filled field variants.
 *
 * @property outlined Whether the form fields should be outlined (true) or filled (false). Defaults to false.
 * @param textStyle The [TextStyle] to be applied to the form field.
 * If null, the current [LocalTextStyle] is used.
 * @param shape The [Shape] of the form field.
 * If null, the shape is determined based on the [outlined] property.
 * @param colors The [TextFieldColors] to be applied to the form field.
 * If null, default colors are used based on the [outlined] property.
 * @param supportingTextColor The color of supporting text (e.g., helper text).
 * If null, the default content color is used.
 * @param errorTextColor The color to be used for error messages.
 * If null, the default error color from [MaterialTheme] is used.
 * @param textSelectionColor The color to be used for text selection handles and background when there is no error.
 * If null, the primary color from [MaterialTheme] is used.
 * @param textSelectionErrorColor The color to be used for text selection handles and background when there is an error.
 * If null, the error color from [MaterialTheme] is used.
 */
class CarlosFieldConfigs(
    val outlined: Boolean = false,
    textStyle: TextStyle? = null,
    shape: Shape? = null,
    colors: TextFieldColors? = null,
    supportingTextColor: Color? = null,
    errorTextColor: Color? = null,
    textSelectionColor: Color? = null,
    textSelectionErrorColor: Color? = null
) {
    // Backing properties to store optional parameters
    private val textStyleParam = textStyle
    private val shapeParam = shape
    private val colorsParam = colors
    private val supportingTextColorParam = supportingTextColor
    private val errorTextColorParam = errorTextColor
    private val textSelectionColorParam = textSelectionColor
    private val textSelectionErrorColorParam = textSelectionErrorColor

    /**
     * The [TextStyle] to be applied to the form field.
     * If not specified, defaults to the current [LocalTextStyle].
     */
    val textStyle: TextStyle
        @Composable
        get() = textStyleParam ?: LocalTextStyle.current

    /**
     * The [Shape] of the form field.
     * If not specified, defaults to [TextFieldDefaults.OutlinedTextFieldShape] or [TextFieldDefaults.TextFieldShape]
     * based on whether the field is outlined or filled.
     */
    val shape: Shape
        @Composable
        get() = shapeParam ?: if (outlined) {
            TextFieldDefaults.OutlinedTextFieldShape
        } else {
            TextFieldDefaults.TextFieldShape
        }

    /**
     * The [TextFieldColors] to be applied to the form field.
     * If not specified, defaults to [TextFieldDefaults.outlinedTextFieldColors] or [TextFieldDefaults.textFieldColors]
     * based on whether the field is outlined or filled.
     */
    val colors: TextFieldColors
        @Composable
        get() = colorsParam ?: if (outlined) {
            TextFieldDefaults.outlinedTextFieldColors()
        } else {
            TextFieldDefaults.textFieldColors()
        }

    /**
     * The color of supporting text, such as helper text.
     * If not specified, defaults to the current content color with the current content alpha.
     */
    val supportingTextColor: Color
        @Composable
        get() = supportingTextColorParam
            ?: LocalContentColor.current.copy(LocalContentAlpha.current)

    /**
     * The color to be used for error messages.
     * If not specified, defaults to the error color from the current [MaterialTheme].
     */
    val errorTextColor: Color
        @Composable
        get() = errorTextColorParam ?: MaterialTheme.colors.error

    /**
     * Returns the [TextSelectionColors] to be used for text selection handles and background.
     *
     * @param isError If true, applies error colors to text selection. Otherwise, applies normal text selection colors.
     * @return A [TextSelectionColors] instance with the appropriate colors for text selection.
     */
    @Composable
    fun textSelectionColors(isError: Boolean): TextSelectionColors {
        val textSelectionColor = if (isError) {
            textSelectionErrorColorParam ?: MaterialTheme.colors.error
        } else {
            textSelectionColorParam ?: MaterialTheme.colors.primary
        }
        return TextSelectionColors(
            handleColor = textSelectionColor,
            backgroundColor = textSelectionColor.copy(alpha = 0.3f)
        )
    }
}

/**
 * A composition local provider for [CarlosFieldConfigs].
 * This allows for a consistent configuration of form fields across a composable hierarchy.
 */
val LocalCarlosConfigs = staticCompositionLocalOf { CarlosFieldConfigs() }
