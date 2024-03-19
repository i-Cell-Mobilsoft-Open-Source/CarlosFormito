package com.icell.external.carlosformito.ui.field.base

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import com.icell.external.carlosformito.ui.theme.LocalCarlosColors

@Composable
fun TextFieldSupportingText(
    modifier: Modifier = Modifier,
    supportingText: CharSequence
) {
    val textColor = LocalCarlosColors.current.supportingTextColor

    when (supportingText) {
        is String -> {
            Text(
                modifier = modifier,
                text = supportingText,
                style = MaterialTheme.typography.caption,
                color = textColor
            )
        }

        is AnnotatedString -> {
            Text(
                modifier = modifier,
                text = supportingText,
                style = MaterialTheme.typography.caption,
                color = textColor
            )
        }

        else -> {
            error("Unsupported type [${supportingText.javaClass.simpleName}] for supportingText!")
        }
    }
}
