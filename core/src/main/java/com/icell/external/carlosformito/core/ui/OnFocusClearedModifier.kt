package com.icell.external.carlosformito.core.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged

/**
 * Extension function for [Modifier] that triggers a callback when the focus is cleared.
 *
 * This function tracks the focus state of a composable and calls the [onFocusCleared] lambda
 * when the composable loses focus after having focus.
 *
 * @param onFocusCleared Lambda function to be called when the focus is cleared.
 * @return The modified [Modifier] with the focus change behavior.
 */
fun Modifier.onFocusCleared(onFocusCleared: () -> Unit): Modifier = composed {
    var recentlyHadFocus by remember { mutableStateOf(false) }

    this.onFocusChanged { state ->
        if (recentlyHadFocus && !state.hasFocus) {
            onFocusCleared()
        }
        recentlyHadFocus = state.hasFocus
    }
}
