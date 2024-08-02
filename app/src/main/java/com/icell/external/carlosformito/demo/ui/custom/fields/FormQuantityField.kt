package com.icell.external.carlosformito.demo.ui.custom.fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.extension.collectFieldState
import com.icell.external.carlosformito.ui.extension.errorMessage
import com.icell.external.carlosformito.ui.field.base.TextFieldSupportingText
import com.icell.external.carlosformito.ui.field.base.TrackVisibilityEffect
import com.icell.external.carlosformito.ui.util.testId

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FormQuantityField(
    modifier: Modifier = Modifier,
    fieldItem: FormFieldItem<Int>,
    enabled: Boolean = true,
    minQuantity: Int = Int.MIN_VALUE,
    maxQuantity: Int = Int.MAX_VALUE,
    supportingText: CharSequence? = null
) {
    val state by fieldItem.collectFieldState()

    TrackVisibilityEffect { visible ->
        fieldItem.onFieldVisibilityChanged(visible)
    }

    Column(
        modifier = modifier
    ) {
        Card(
            elevation = 0.dp,
            enabled = false,
            onClick = {},
            backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.06f)
        ) {
            val currentValue = state.value ?: 0

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    enabled = enabled && currentValue > minQuantity,
                    onClick = {
                        fieldItem.onFieldValueChanged(currentValue - 1)
                    }
                ) {
                    Icon(
                        Icons.Default.RemoveCircleOutline,
                        contentDescription = "Minus",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = currentValue.toString(),
                    style = MaterialTheme.typography.h6.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    enabled = enabled && currentValue < maxQuantity,
                    onClick = {
                        fieldItem.onFieldValueChanged(currentValue + 1)
                    }
                ) {
                    Icon(
                        Icons.Default.AddCircleOutline,
                        contentDescription = "Plus",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
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
