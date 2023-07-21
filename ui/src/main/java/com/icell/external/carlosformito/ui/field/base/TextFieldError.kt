package com.icell.external.carlosformito.ui.field.base

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

@Composable
fun TextFieldError(
    modifier: Modifier = Modifier,
    errorText: CharSequence,
    textColor: Color
) {
    when (errorText) {
        is String -> {
            Text(
                text = errorText,
                color = textColor,
                modifier = modifier,
                style = MaterialTheme.typography.caption
            )
        }

        is AnnotatedString -> {
            Text(
                text = errorText,
                color = textColor,
                modifier = modifier,
                style = MaterialTheme.typography.caption
            )
        }

        else -> {
            error("Unsupported type [${errorText.javaClass.simpleName}] for errorText!")
        }
    }
}
