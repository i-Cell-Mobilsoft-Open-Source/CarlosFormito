package com.icell.external.carlosformito.demo.ui.custom.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.ui.TrackVisibilityEffect
import com.icell.external.carlosformito.core.ui.extensions.collectFieldState
import com.icell.external.carlosformito.core.ui.extensions.errorMessage
import com.icell.external.carlosformito.core.ui.testId
import com.icell.external.carlosformito.ui.field.base.TextFieldSupportingText

@Composable
fun FormCheckboxField(
    modifier: Modifier = Modifier,
    text: String,
    fieldItem: FormFieldItem<Boolean>,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.body2,
    colors: CheckboxColors = CheckboxDefaults.colors(),
    supportingText: CharSequence? = null
) {
    val state by fieldItem.collectFieldState()
    val currentValue = state.value ?: false

    TrackVisibilityEffect { visible ->
        fieldItem.onFieldVisibilityChanged(visible)
    }

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = currentValue,
                enabled = enabled,
                colors = colors,
                onCheckedChange = { checked ->
                    fieldItem.onFieldValueChanged(checked)
                }
            )
            Text(
                text = text,
                style = textStyle,
                color = TextFieldDefaults.textFieldColors().textColor(enabled).value,
                modifier = Modifier.fillMaxWidth()
            )
        }

        val displayedSupportingText = if (state.isError) {
            state.errorMessage() ?: supportingText
        } else {
            supportingText
        }

        AnimatedVisibility(visible = !displayedSupportingText.isNullOrBlank()) {
            TextFieldSupportingText(
                modifier = Modifier
                    .testId("text_supported")
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
                    .padding(horizontal = 16.dp),
                isError = state.isError,
                supportingText = displayedSupportingText ?: ""
            )
        }
    }
}
