package com.icell.external.carlosformito.ui.password

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.FormManagerViewModel
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_CONFIRM_PASSWORD
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_PASSWORD
import com.icell.external.carlosformito.ui.password.validator.ConfirmPasswordMatchValidator
import com.icell.external.carlosformito.ui.util.extension.getFieldValue
import kotlinx.coroutines.launch

class SetPasswordViewModel : FormManagerViewModel(SetPasswordFields.build()) {

    private val confirmPasswordMatchValidator = ConfirmPasswordMatchValidator()

    override suspend fun validateForm(): Boolean {
        val independentFieldsAreValid = super.validateForm()

        val password = getFieldValue<String>(KEY_PASSWORD).orEmpty()
        val confirmPassword = getFieldValue<String>(KEY_CONFIRM_PASSWORD).orEmpty()
        val passwordMatchResult = confirmPasswordMatchValidator.validate(password, confirmPassword)

        if (passwordMatchResult is FormFieldValidationResult.Invalid) {
            setFieldInvalid(KEY_CONFIRM_PASSWORD, passwordMatchResult)
        }

        return independentFieldsAreValid && passwordMatchResult is FormFieldValidationResult.Valid
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("SetPasswordViewModel", "Form is valid")
            }
        }
    }
}
