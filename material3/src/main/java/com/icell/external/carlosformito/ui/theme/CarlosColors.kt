package com.icell.external.carlosformito.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
        get() = fieldColorParam ?: TextFieldDefaults.colors()

    val supportingTextColor: Color
        @Composable
        get() = supportingTextColorParam ?: LocalContentColor.current

    val errorTextColor: Color
        @Composable
        get() = errorTextColorParam ?: MaterialTheme.colorScheme.error

    @Composable
    fun textSelectionColors(isError: Boolean): TextSelectionColors {
        val textSelectionColor = if (isError) {
            textSelectionErrorColorParam ?: MaterialTheme.colorScheme.error
        } else {
            textSelectionColorParam ?: MaterialTheme.colorScheme.primary
        }
        return TextSelectionColors(
            handleColor = textSelectionColor,
            backgroundColor = textSelectionColor.copy(alpha = 0.3f)
        )
    }
}

val LocalCarlosColors = staticCompositionLocalOf { CarlosColors() }
