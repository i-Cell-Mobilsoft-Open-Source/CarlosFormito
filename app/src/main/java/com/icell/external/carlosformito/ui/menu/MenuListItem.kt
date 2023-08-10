package com.icell.external.carlosformito.ui.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MenuListItem(
    title: String,
    modifier: Modifier = Modifier,
    bodyContent: @Composable ColumnScope.() -> Unit = {},
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    modifier = Modifier.weight(1.0f),
                    style = MaterialTheme.typography.body1
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "➜",
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = contentColorFor(MaterialTheme.colors.surface)
                    )
                )
            }
            bodyContent()
        }
    }
}
