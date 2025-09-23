package hu.icellmobilsoft.carlosformito.demo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import hu.icellmobilsoft.carlosformito.ui.theme.CarlosFieldConfigs
import hu.icellmobilsoft.carlosformito.ui.theme.CarlosFormatDefaults
import hu.icellmobilsoft.carlosformito.ui.theme.LocalCarlosConfigs
import hu.icellmobilsoft.carlosformito.ui.theme.LocalCarlosFormats
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

private val darkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Color.Black,
    surface = Color.Black
)

private val lightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = Color.White,
    surface = Color.White
)

@OptIn(FormatStringsInDatetimeFormats::class)
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
        LocalCarlosConfigs provides CarlosFieldConfigs(
            outlined = false
        ),
        LocalCarlosFormats provides CarlosFormatDefaults(
            dateFormat = LocalDate.Format {
                byUnicodePattern("yyyy-MM-dd")
            },
            timeFormat = LocalTime.Formats.ISO,
            is24HourFormat = true
        )
    ) {
        MaterialTheme(
            colors = colors,
            typography = CarlosTypography,
            shapes = CarlosShapes,
            content = content
        )
    }
}
