package hu.icellmobilsoft.carlosformito.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * A composable function that observes lifecycle events of the given [lifecycleOwner]
 * and triggers a callback when a lifecycle event occurs.
 *
 * @param lifecycleOwner the [LifecycleOwner] whose lifecycle events are being observed.
 * @param onLifecycleEvent a lambda function invoked with the [Lifecycle.Event]
 * whenever the lifecycle of the [lifecycleOwner] changes.
 */
@Composable
internal fun LifecycleObserver(
    lifecycleOwner: LifecycleOwner,
    onLifecycleEvent: (Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            onLifecycleEvent(event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
