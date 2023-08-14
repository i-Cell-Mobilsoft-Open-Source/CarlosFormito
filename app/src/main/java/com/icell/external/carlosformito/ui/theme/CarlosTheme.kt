package com.icell.external.carlosformito.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val darkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Grey900,
    surface = Color.Black
)

private val lightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Grey100,
    surface = Color.White
)

@Composable
fun CarlosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    /**
     * Here you can define your custom colors, icons and formats used by built-in field types
     */
    CompositionLocalProvider(
        LocalCarlosColors provides CarlosColors(),
        LocalCarlosFormats provides CarlosFormattingDefaults(
            dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG),
            timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        ),
        LocalCarlosIcons provides DefaultOutlinedCarlosIcons
    ) {
        MaterialTheme(
            colors = colors,
            typography = CarlosTypography,
            shapes = CarlosShapes,
            content = content
        )
    }
}
