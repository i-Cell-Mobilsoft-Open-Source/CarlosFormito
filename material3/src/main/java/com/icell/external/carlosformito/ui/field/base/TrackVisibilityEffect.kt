package com.icell.external.carlosformito.ui.field.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun TrackVisibilityEffect(onVisibilityChanged: (visible: Boolean) -> Unit) {
    DisposableEffect(Unit) {
        onVisibilityChanged(true)
        onDispose {
            onVisibilityChanged(false)
        }
    }
}
