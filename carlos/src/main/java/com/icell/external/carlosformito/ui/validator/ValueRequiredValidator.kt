package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.api.validator.FormFieldValidator
import com.icell.external.carlosformito.api.validator.RequiresFieldValue

class ValueRequiredValidator<T>(
    @StringRes
    private val errorMessageId: Int = R.string.carlos_lbl_validator_validator_value_required
) : FormFieldValidator<T>, RequiresFieldValue {

    override suspend fun validate(value: T?): FormFieldValidationResult {
        return if (value == null || value is String && value.isBlank()) {
            FormFieldValidationResult.Invalid.Message(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
