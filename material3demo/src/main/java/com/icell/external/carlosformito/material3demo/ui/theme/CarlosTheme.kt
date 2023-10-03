package com.icell.external.carlosformito.material3demo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.icell.external.carlosformito.ui.theme.CarlosColors
import com.icell.external.carlosformito.ui.theme.CarlosFormattingDefaults
import com.icell.external.carlosformito.ui.theme.DefaultOutlinedCarlosIcons
import com.icell.external.carlosformito.ui.theme.LocalCarlosColors
import com.icell.external.carlosformito.ui.theme.LocalCarlosFormats
import com.icell.external.carlosformito.ui.theme.LocalCarlosIcons
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

private val darkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val lightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun CarlosFormitoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    /**
     * Here you can define your custom colors, icons and formats used by built-in field types
     */
    CompositionLocalProvider(
        LocalCarlosColors provides CarlosColors(),
        LocalCarlosFormats provides CarlosFormattingDefaults(
            dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG),
            timeFormatter = DateTimeFormatter.ofPattern("hh:mm"),
            is24HourFormat = false
        ),
        LocalCarlosIcons provides DefaultOutlinedCarlosIcons
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
