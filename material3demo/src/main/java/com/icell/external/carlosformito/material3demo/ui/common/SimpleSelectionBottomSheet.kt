package com.icell.external.carlosformito.material3demo.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SimpleSelectionBottomSheet(
    items: List<T>,
    itemText: (index: Int, item: T) -> String,
    onItemSelected: (index: Int, item: T) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        items.forEachIndexed { index, item ->
            Text(
                text = itemText(index, item),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemSelected(index, item)
                    }
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            if (index != items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
        }
    }
}
