package hu.icellmobilsoft.carlosformito.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import hu.icellmobilsoft.carlosformito.core.api.FormFieldItem
import hu.icellmobilsoft.carlosformito.core.api.FormManager
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldState
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Extension function for [FormManager] that collects the state of a form field with the given ID.
 *
 * This function retrieves the field item of type [T] by its ID and then collects its state.
 *
 * @param T The type of the form field.
 * @param id The ID of the form field.
 * @param context The [CoroutineContext] in which to collect. Defaults to [EmptyCoroutineContext].
 * @return A [State] object containing the [FormFieldState] of the specified form field.
 */
@Composable
fun <T> FormManager.collectFieldState(
    id: String,
    context: CoroutineContext = EmptyCoroutineContext
): State<FormFieldState<T>> = getFieldItem<T>(id).collectFieldState(context)

/**
 * Extension function for [FormFieldItem] that collects the state of the form field.
 *
 * This function collects the [FormFieldState] of the form field as a [State] object
 * using the [collectAsState] function.
 *
 * @param T The type of the form field.
 * @param context The [CoroutineContext] in which to collect. Defaults to [EmptyCoroutineContext].
 * @return A [State] object containing the [FormFieldState] of the form field.
 */
@Composable
fun <T> FormFieldItem<T>.collectFieldState(
    context: CoroutineContext = EmptyCoroutineContext
): State<FormFieldState<T>> = fieldState.collectAsState(context)

/**
 * Extension function for [FormManager] that collects the current value of a form field
 * with the given ID.
 *
 * This function retrieves the field item of type [T] by its ID and collects its state
 * to return the current value directly.
 *
 * @param T The type of the form field.
 * @param id The ID of the form field.
 * @param context The [CoroutineContext] in which to collect. Defaults to [EmptyCoroutineContext].
 * @return The current value of the form field, or `null` if unavailable.
 */
@Composable
fun <T> FormManager.collectFieldValue(
    id: String,
    context: CoroutineContext = EmptyCoroutineContext
): T? = getFieldItem<T>(id).collectFieldValue(context)

/**
 * Extension function for [FormFieldItem] that collects and returns the current value of the form field.
 *
 * This function collects the [FormFieldState] of the form field and returns its [FormFieldState.value].
 *
 * @param T The type of the form field.
 * @param context The [CoroutineContext] in which to collect. Defaults to [EmptyCoroutineContext].
 * @return The current value of the form field, or `null` if unavailable.
 */
@Composable
fun <T> FormFieldItem<T>.collectFieldValue(
    context: CoroutineContext = EmptyCoroutineContext
): T? = collectFieldState(context).value.value
