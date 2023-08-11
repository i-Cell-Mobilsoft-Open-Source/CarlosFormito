package com.icell.external.carlosformito.ui.password.validator

import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult

class ConfirmPasswordMatchValidator {

    fun validate(password: String, confirmPassword: String): FormFieldValidationResult {
        return if (password != confirmPassword) {
            FormFieldValidationResult.Invalid.Message(R.string.confirm_password_not_match_error)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
