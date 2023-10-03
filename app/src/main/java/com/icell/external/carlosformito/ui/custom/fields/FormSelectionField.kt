package com.icell.external.carlosformito.ui.custom.fields

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.BaseFieldFrame

@Composable
fun <T> FormSelectionField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<T>,
    enabled: Boolean = true,
    onSelectValue: () -> Unit,
    displayedValue: (T?) -> String,
    supportingText: AnnotatedString? = null
) {
    val state by fieldItem.collectFieldState()
    BaseFieldFrame(
        modifier = modifier,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        supportingText = supportingText
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialTheme.colors.primary.copy(alpha = 0.06f))
                .clickable(enabled = enabled, onClick = onSelectValue)
                .padding(16.dp)
        ) {
            Text(
                text = displayedValue(state.value),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Filled.KeyboardArrowRight, null)
        }
    }
}
