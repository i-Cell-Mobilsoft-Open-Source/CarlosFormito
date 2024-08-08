package com.icell.external.carlosformito.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult

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
