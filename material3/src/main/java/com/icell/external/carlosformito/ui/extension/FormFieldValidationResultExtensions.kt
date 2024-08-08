package com.icell.external.carlosformito.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
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
