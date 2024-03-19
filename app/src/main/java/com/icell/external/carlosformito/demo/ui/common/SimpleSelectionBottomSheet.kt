package com.icell.external.carlosformito.demo.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SimpleSelectionBottomSheet(
    items: List<T>,
    itemText: (index: Int, item: T) -> String,
    onItemSelected: (index: Int, item: T) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        items.forEachIndexed { index, item ->
            Text(
                text = itemText(index, item),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemSelected(index, item)
                    }
                    .padding(16.dp),
                style = MaterialTheme.typography.body1
            )
            if (index != items.lastIndex) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                )
            }
        }
    }
}
