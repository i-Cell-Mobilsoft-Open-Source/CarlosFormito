package com.icell.external.carlosformito.ui.custom.fields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.commonui.extension.collectFieldState
import com.icell.external.carlosformito.commonui.extension.errorMessage
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.ui.field.base.BaseFieldFrame

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
    BaseFieldFrame(
        modifier = modifier,
        isError = state.isError,
        errorMessage = state.errorMessage(),
        supportingText = supportingText
    ) {
        Card(
            elevation = 0.dp,
            modifier = modifier,
            enabled = false,
            onClick = {}
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
                        painterResource(id = R.drawable.ic_remove_circle),
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
                        painterResource(id = R.drawable.ic_add_circle),
                        contentDescription = "Plus",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
