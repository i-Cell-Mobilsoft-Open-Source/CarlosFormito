package hu.icellmobilsoft.carlosformito.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * A composable function that tracks the visibility state of its content and triggers a callback
 * when the visibility changes.
 *
 * @param onVisibilityChanged a lambda function to be called when the visibility of the content changes.
 * It is called with visible = `true` when the content is composed for the first time
 * and called with `false` when the content leaves composition (onDispose),
 * unless the last observed lifecycle event was [Lifecycle.Event.ON_STOP].
 *
 * If the last observed lifecycle event is [Lifecycle.Event.ON_STOP],
 * it indicates that the composable was disposed due to a navigation event,
 * which should not trigger a visible = `false` callback.
 */
@Composable
fun TrackVisibilityEffect(onVisibilityChanged: (visible: Boolean) -> Unit) {
    var lastLifeCycleEvent by remember { mutableStateOf<Lifecycle.Event?>(null) }
    LifecycleObserver(
        lifecycleOwner = LocalLifecycleOwner.current
    ) { lifecycleEvent ->
        lastLifeCycleEvent = lifecycleEvent
    }

    DisposableEffect(Unit) {
        onVisibilityChanged(true)
        onDispose {
            if (lastLifeCycleEvent != Lifecycle.Event.ON_STOP) {
                onVisibilityChanged(false)
            }
            lastLifeCycleEvent = null
        }
    }
}
