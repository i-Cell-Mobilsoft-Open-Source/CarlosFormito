package com.icell.external.carlosformito.ui.password

import com.icell.external.carlosformito.core.FormManagerImpl
import com.icell.external.carlosformito.ui.extension.getFieldValue
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_CONFIRM_PASSWORD
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_PASSWORD
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SetPasswordFormManager : FormManagerImpl(SetPasswordFields.build()) {

    private val mutablePasswordMatchError = MutableStateFlow(false)
    val passwordMatchError = mutablePasswordMatchError.asStateFlow()

    override fun <T> onFieldValueChanged(id: String, value: T?) {
        super.onFieldValueChanged(id, value)

        mutablePasswordMatchError.value = false
    }

    override suspend fun validateForm(): Boolean {
        val independentFieldsAreValid = super.validateForm()

        val password = getFieldValue<String>(KEY_PASSWORD).orEmpty()
        val confirmPassword = getFieldValue<String>(KEY_CONFIRM_PASSWORD).orEmpty()
        if (password != confirmPassword) {
            mutablePasswordMatchError.value = true
        }

        return independentFieldsAreValid
    }
}
