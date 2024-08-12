package com.icell.external.carlosformito.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult

/**
 * Extension function for [FormFieldValidationResult.Invalid] that retrieves the error message.
 *
 * This function checks the type of the invalid result and formats the error message accordingly:
 * - If the result is [FormFieldValidationResult.Invalid.MessageWithArgs], it uses the provided message ID
 *   and arguments to format the error message using [stringResource].
 * - Otherwise, it uses the message ID directly to retrieve the error message using [stringResource].
 *
 * @return The formatted error message as a [String] if the message ID is not null, otherwise null.
 */
@Composable
@ReadOnlyComposable
fun FormFieldValidationResult.Invalid.errorMessage(): String? {
    return when (this) {
        is FormFieldValidationResult.Invalid.MessageWithArgs -> {
            stringResource(id = errorMessageId, *formatArgs.toTypedArray())
        }

        else -> {
            errorMessageId?.let { id -> stringResource(id = id) }
        }
    }
}

/**
 * Extension function for [FormFieldState] that retrieves the error message if the validation result is invalid.
 *
 * @return The error message as a [String] if the validation result is invalid, otherwise null.
 */
@Composable
@ReadOnlyComposable
fun FormFieldState<*>.errorMessage(): String? {
    return (validationResult as? FormFieldValidationResult.Invalid)?.errorMessage()
}

/**
 * Extension function for [FormManager] that collects the state of a form field with the given ID.
 *
 * This function retrieves the field item of type [T] by its ID and then collects its state.
 *
 * @param T The type of the form field.
 * @param id The ID of the form field.
 * @return A [State] object containing the [FormFieldState] of the specified form field.
 */
@Composable
fun <T> FormManager.collectFieldState(id: String): State<FormFieldState<T>> =
    getFieldItem<T>(id).collectFieldState()

/**
 * Extension function for [FormFieldItem] that collects the state of the form field.
 *
 * This function collects the [FormFieldState] of the form field as a [State] object
 * using the [collectAsState] function.
 *
 * @param T The type of the form field.
 * @return A [State] object containing the [FormFieldState] of the form field.
 */
@Composable
fun <T> FormFieldItem<T>.collectFieldState(): State<FormFieldState<T>> = fieldState.collectAsState()
