package com.icell.external.carlosformito.ui.util

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged

fun Modifier.onFocusCleared(onFocusCleared: () -> Unit): Modifier = composed {
    var recentlyHadFocus by remember { mutableStateOf(false) }

    this.onFocusChanged { state ->
        if (recentlyHadFocus && !state.hasFocus) {
            onFocusCleared()
        }
        recentlyHadFocus = state.hasFocus
    }
}
