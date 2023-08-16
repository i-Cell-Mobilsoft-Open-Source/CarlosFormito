package com.icell.external.carlosformito.ui.util.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult

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
