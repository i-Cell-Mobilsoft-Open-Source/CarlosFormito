package com.icell.external.carlosformito.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class CarlosColors(
    fieldColors: TextFieldColors? = null,
    supportingTextColor: Color? = null,
    errorTextColor: Color? = null,
    textSelectionColor: Color? = null,
    textSelectionErrorColor: Color? = null
) {
    private val fieldColorParam = fieldColors
    private val supportingTextColorParam = supportingTextColor
    private val errorTextColorParam = errorTextColor
    private val textSelectionColorParam = textSelectionColor
    private val textSelectionErrorColorParam = textSelectionErrorColor

    val textFieldColors: TextFieldColors
        @Composable
        get() = fieldColorParam ?: TextFieldDefaults.textFieldColors()

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

val LocalCarlosColors = staticCompositionLocalOf { CarlosColors() }
