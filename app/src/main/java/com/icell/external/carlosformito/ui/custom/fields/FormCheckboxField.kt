package com.icell.external.carlosformito.ui.custom.fields

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.field.base.BaseFieldFrame
import com.icell.external.carlosformito.ui.util.extension.collectFieldState
import com.icell.external.carlosformito.ui.util.extension.errorMessage

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

    BaseFieldFrame(
        modifier = modifier,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        supportingText = supportingText
    ) {
        Row(
            modifier = modifier,
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
    }
}
