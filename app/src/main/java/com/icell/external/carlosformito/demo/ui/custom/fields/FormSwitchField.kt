package com.icell.external.carlosformito.demo.ui.custom.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.TextFieldSupportingText
import com.icell.external.carlosformito.ui.field.base.TrackVisibilityEffect
import com.icell.external.carlosformito.ui.util.testId

@Composable
fun FormSwitchField(
    modifier: Modifier = Modifier,
    text: String,
    fieldItem: FormFieldItem<Boolean>,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    colors: SwitchColors = SwitchDefaults.colors(),
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
            Text(
                text = text,
                style = textStyle,
                color = TextFieldDefaults.textFieldColors().textColor(enabled).value,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = currentValue,
                enabled = enabled,
                colors = colors,
                onCheckedChange = { checked ->
                    fieldItem.onFieldValueChanged(checked)
                }
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
