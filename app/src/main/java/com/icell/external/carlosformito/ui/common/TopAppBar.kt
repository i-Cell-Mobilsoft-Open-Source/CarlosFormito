package com.icell.external.carlosformito.ui.common

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun CarlosTopAppBar(
    title: String,
    onNavigationIconPressed: () -> Unit
) {
    TopAppBar(
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.surface,
        navigationIcon = {
            IconButton(onClick = onNavigationIconPressed) {
                Icon(Icons.Filled.ArrowBack, "Navigate up")
            }
        },
        title = {
            Text(title)
        }
    )
}
