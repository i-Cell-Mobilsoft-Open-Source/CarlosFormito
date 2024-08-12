package com.icell.external.carlosformito.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * A composable function that tracks the visibility state of its content and triggers a callback
 * when the visibility changes.
 *
 * @param onVisibilityChanged a lambda function to be called when the visibility of the content changes.
 * It is called with visible = `true` when the content is composed for the first time
 * And called with `false` when the content leaves composition (onDispose)
 */
@Composable
fun TrackVisibilityEffect(onVisibilityChanged: (visible: Boolean) -> Unit) {
    DisposableEffect(Unit) {
        onVisibilityChanged(true)
        onDispose {
            onVisibilityChanged(false)
        }
    }
}
