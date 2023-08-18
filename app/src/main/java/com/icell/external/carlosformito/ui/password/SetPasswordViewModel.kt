package com.icell.external.carlosformito.ui.password

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.ui.common.FormManagerViewModel
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_CONFIRM_PASSWORD
import com.icell.external.carlosformito.ui.password.SetPasswordFields.KEY_PASSWORD
import com.icell.external.carlosformito.ui.util.extension.getFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SetPasswordViewModel : FormManagerViewModel(SetPasswordFields.build()) {

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

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("SetPasswordViewModel", "Form is valid")
            }
        }
    }
}
