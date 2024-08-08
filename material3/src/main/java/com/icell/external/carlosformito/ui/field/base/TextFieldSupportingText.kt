package com.icell.external.carlosformito.ui.field.base

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import com.icell.external.carlosformito.ui.theme.LocalCarlosConfigs

/**
 * A composable function that displays supporting text below a text field. The text can change color
 * based on whether there is an error state or not.
 *
 * @param modifier a [Modifier] to be applied to the supporting text.
 * @param isError indicates if the text field is in an error state. Changes the color of the supporting text.
 * @param supportingText the supporting text to be displayed. Can be either a [String] or [AnnotatedString].
 */
@Composable
fun TextFieldSupportingText(
    modifier: Modifier = Modifier,
    isError: Boolean,
    supportingText: CharSequence,
) {
    val carlosConfigs = LocalCarlosConfigs.current
    val textColor: Color by animateColorAsState(
        if (isError) {
            carlosConfigs.errorTextColor
        } else {
            carlosConfigs.supportingTextColor
        },
        label = "Text color animation"
    )

    when (supportingText) {
        is String -> {
            Text(
                modifier = modifier.animateContentSize(),
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
        }

        is AnnotatedString -> {
            Text(
                modifier = modifier.animateContentSize(),
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
        }

        else -> {
            error("Unsupported type [${supportingText.javaClass.simpleName}] for supportingText!")
        }
    }
}
