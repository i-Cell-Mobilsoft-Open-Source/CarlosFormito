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
    private val textStyleParam = textStyle
    private val shapeParam = shape
    private val colorsParam = colors
    private val supportingTextColorParam = supportingTextColor
    private val errorTextColorParam = errorTextColor
    private val textSelectionColorParam = textSelectionColor
    private val textSelectionErrorColorParam = textSelectionErrorColor

    val textStyle: TextStyle
        @Composable
        get() = textStyleParam ?: LocalTextStyle.current

    val shape: Shape
        @Composable
        get() = shapeParam ?: if (outlined) {
            TextFieldDefaults.OutlinedTextFieldShape
        } else {
            TextFieldDefaults.TextFieldShape
        }

    val colors: TextFieldColors
        @Composable
        get() = colorsParam ?: if (outlined) {
            TextFieldDefaults.outlinedTextFieldColors()
        } else {
            TextFieldDefaults.textFieldColors()
        }

    val supportingTextColor: Color
        @Composable
        get() = supportingTextColorParam
            ?: LocalContentColor.current.copy(LocalContentAlpha.current)

    val errorTextColor: Color
        @Composable
        get() = errorTextColorParam ?: MaterialTheme.colors.error

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

val LocalCarlosConfigs = staticCompositionLocalOf { CarlosFieldConfigs() }
