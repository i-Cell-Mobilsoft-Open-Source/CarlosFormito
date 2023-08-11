package com.icell.external.carlosformito.ui.field.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BaseFieldFrame(
    modifier: Modifier = Modifier,
    isError: Boolean,
    errorMessage: CharSequence?,
    supportingText: CharSequence?,
    fieldContent: @Composable BoxScope.() -> Unit
) {
    Column(modifier) {
        Box {
            fieldContent()
        }
        AnimatedVisibility(visible = isError) {
            TextFieldError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                errorText = errorMessage ?: ""
            )
        }
        if (supportingText != null) {
            TextFieldSupportingText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
                    .padding(horizontal = 16.dp),
                supportingText = supportingText
            )
        }
    }
}
