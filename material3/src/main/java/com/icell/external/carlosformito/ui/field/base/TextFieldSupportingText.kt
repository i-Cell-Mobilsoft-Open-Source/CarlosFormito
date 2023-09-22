package com.icell.external.carlosformito.ui.field.base

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString

@Composable
fun TextFieldSupportingText(
    modifier: Modifier = Modifier,
    isError: Boolean,
    supportingText: CharSequence?,
    errorMessage: CharSequence?
) {
    when (val displayedText = if (isError) errorMessage else supportingText) {
        is String -> {
            Text(
                modifier = modifier,
                text = displayedText
            )
        }

        is AnnotatedString -> {
            Text(
                modifier = modifier,
                text = displayedText
            )
        }

        else -> {
            displayedText?.let {
                error("Unsupported type [${displayedText.javaClass.simpleName}] for supportingText!")
            }
        }
    }
}
