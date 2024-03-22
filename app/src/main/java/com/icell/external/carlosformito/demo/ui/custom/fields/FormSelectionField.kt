package com.icell.external.carlosformito.demo.ui.custom.fields

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.icell.external.carlosformito.ui.field.base.TrackVisibilityEffect

@OptIn(ExperimentalMaterialApi::class)
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

    TrackVisibilityEffect { visible ->
        fieldItem.onFieldVisibilityChanged(visible)
    }

    BaseFieldFrame(
        modifier = modifier,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        supportingText = supportingText
    ) {
        Card(
            elevation = 0.dp,
            modifier = modifier,
            enabled = enabled,
            onClick = onSelectValue,
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.06f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = displayedValue(state.value),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
            }
        }
    }
}
