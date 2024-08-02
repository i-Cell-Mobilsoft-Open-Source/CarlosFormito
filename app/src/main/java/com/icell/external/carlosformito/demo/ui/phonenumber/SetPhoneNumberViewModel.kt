package com.icell.external.carlosformito.demo.ui.phonenumber

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.CarlosFormManager
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

class SetPhoneNumberViewModel(
    validationStrategy: FormFieldValidationStrategy,
) : ViewModel(), FormManager by CarlosFormManager(SetPhoneNumberFields.build(), validationStrategy) {

    init {
        viewModelScope.launch {
            initFormManager()
        }
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("SetPhoneNumberViewModel", "Form is valid")
            }
        }
    }
}
