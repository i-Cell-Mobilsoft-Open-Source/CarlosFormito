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
import com.icell.external.carlosformito.ui.theme.LocalCarlosColors

@Composable
fun TextFieldSupportingText(
    modifier: Modifier = Modifier,
    isError: Boolean,
    supportingText: CharSequence,
) {
    val carlosColors = LocalCarlosColors.current
    val textColor: Color by animateColorAsState(
        if (isError) {
            carlosColors.errorTextColor
        } else {
            carlosColors.supportingTextColor
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
