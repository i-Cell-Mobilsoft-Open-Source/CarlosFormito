@file:Suppress("MaxLineLength")

package hu.icellmobilsoft.carlosformito.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.icellmobilsoft.carlosformito.core.api.FormFieldItem
import hu.icellmobilsoft.carlosformito.core.api.FormManager
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldState
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Extension function for [FormManager] that collects the state of a form field with the given ID,
 * while being lifecycle-aware.
 *
 * This function retrieves the field item of type [T] by its ID and then collects its state
 * using [collectAsStateWithLifecycle]. The collection automatically respects the provided
 * [lifecycleOwner] and only runs when the lifecycle is at least in the [minActiveState].
 *
 * @param T The type of the form field.
 * @param id The ID of the form field.
 * @param lifecycleOwner The [LifecycleOwner] that controls collection. Defaults to the current composition's owner.
 * @param minActiveState The minimum lifecycle state at which the collection should be active. Defaults to [Lifecycle.State.STARTED].
 * @param context The [CoroutineContext] in which to collect. Defaults to [EmptyCoroutineContext].
 * @return A [State] object containing the [FormFieldState] of the specified form field.
 */
@Composable
fun <T> FormManager.collectFieldStateWithLifecycle(
    id: String,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): State<FormFieldState<T>> =
    getFieldItem<T>(id).collectFieldStateWithLifecycle(lifecycleOwner, minActiveState, context)

/**
 * Extension function for [FormFieldItem] that collects the state of the form field
 * in a lifecycle-aware manner.
 *
 * This function collects the [FormFieldState] of the form field as a [State] object
 * using [collectAsStateWithLifecycle]. The collection automatically respects the provided
 * [lifecycleOwner] and only runs when the lifecycle is at least in the [minActiveState].
 *
 * @param T The type of the form field.
 * @param lifecycleOwner The [LifecycleOwner] that controls collection. Defaults to the current composition's owner.
 * @param minActiveState The minimum lifecycle state at which the collection should be active. Defaults to [Lifecycle.State.STARTED].
 * @param context The [CoroutineContext] in which to collect. Defaults to [EmptyCoroutineContext].
 * @return A [State] object containing the [FormFieldState] of the form field.
 */
@Composable
fun <T> FormFieldItem<T>.collectFieldStateWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): State<FormFieldState<T>> = fieldState.collectAsStateWithLifecycle(lifecycleOwner, minActiveState, context)

/**
 * Extension function for [FormManager] that collects the current value of a form field
 * with the given ID in a lifecycle-aware manner.
 *
 * This function retrieves the field item of type [T] by its ID and collects its value
 * using lifecycle-aware collection. The collection automatically respects the provided
 * [lifecycleOwner] and only runs when the lifecycle is at least in the [minActiveState].
 *
 * @param T The type of the form field.
 * @param id The ID of the form field.
 * @param lifecycleOwner The [LifecycleOwner] that controls collection. Defaults to the current composition's owner.
 * @param minActiveState The minimum lifecycle state at which the collection should be active. Defaults to [Lifecycle.State.STARTED].
 * @param context The [CoroutineContext] in which to collect. Defaults to [EmptyCoroutineContext].
 * @return The current value of the form field, or `null` if unavailable.
 */
@Composable
fun <T> FormManager.collectFieldValueWithLifecycle(
    id: String,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): T? = getFieldItem<T>(id).collectFieldValueWithLifecycle(lifecycleOwner, minActiveState, context)

/**
 * Extension function for [FormFieldItem] that collects and returns the current value of the form field
 * in a lifecycle-aware manner.
 *
 * This function collects the [FormFieldState] of the form field using [collectAsStateWithLifecycle]
 * and returns its [FormFieldState.value].
 *
 * @param T The type of the form field.
 * @param lifecycleOwner The [LifecycleOwner] that controls collection. Defaults to the current composition's owner.
 * @param minActiveState The minimum lifecycle state at which the collection should be active. Defaults to [Lifecycle.State.STARTED].
 * @param context The [CoroutineContext] in which to collect. Defaults to [EmptyCoroutineContext].
 * @return The current value of the form field, or `null` if unavailable.
 */
@Composable
fun <T> FormFieldItem<T>.collectFieldValueWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): T? = collectFieldStateWithLifecycle(lifecycleOwner, minActiveState, context).value.value
