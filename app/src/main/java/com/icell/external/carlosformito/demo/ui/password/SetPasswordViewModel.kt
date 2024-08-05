package com.icell.external.carlosformito.demo.ui.password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.CarlosFormManager
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

class SetPasswordViewModel(
    validationStrategy: FormFieldValidationStrategy,
) : ViewModel(), FormManager by CarlosFormManager(SetPasswordFields.build(), validationStrategy) {

    init {
        viewModelScope.launch {
            initFormManager()
        }
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("SetPasswordViewModel", "Form is valid")
            }
        }
    }
}
