package com.icell.external.carlosformito.ui.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.icell.external.carlosformito.api.model.FormFieldState
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult

@Composable
@ReadOnlyComposable
fun FormFieldState<*>.errorMessage(): String? {
    return (validationResult as? FormFieldValidationResult.Invalid)?.errorMessage()
}
