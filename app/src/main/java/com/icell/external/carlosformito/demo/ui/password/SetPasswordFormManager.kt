package com.icell.external.carlosformito.demo.ui.password

import com.icell.external.carlosformito.core.FormManagerImpl
import com.icell.external.carlosformito.core.api.getFieldValue
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.demo.R
import com.icell.external.carlosformito.demo.ui.password.SetPasswordFields.KEY_CONFIRM_PASSWORD
import com.icell.external.carlosformito.demo.ui.password.SetPasswordFields.KEY_PASSWORD

class SetPasswordFormManager : FormManagerImpl(SetPasswordFields.build()) {

    private val confirmPasswordState = getFieldStateFlow<String>(KEY_CONFIRM_PASSWORD)

    override fun <T> onFieldValueChanged(id: String, value: T?) {
        super.onFieldValueChanged(id, value)

        if (id == KEY_PASSWORD || id == KEY_CONFIRM_PASSWORD) {
            confirmPasswordState.value = confirmPasswordState.value.copy(
                validationResult = null // Clear validation result on value change
            )
        }
    }

    override suspend fun validateForm(): Boolean {
        val independentFieldsAreValid = super.validateForm()

        val password = getFieldValue<String>(KEY_PASSWORD).orEmpty()
        val confirmPassword = getFieldValue<String>(KEY_CONFIRM_PASSWORD).orEmpty()
        if (password != confirmPassword) {
            confirmPasswordState.value = confirmPasswordState.value.copy(
                validationResult = FormFieldValidationResult.Invalid.Message(R.string.confirm_password_not_match_error)
            )
        }

        return independentFieldsAreValid
    }
}
