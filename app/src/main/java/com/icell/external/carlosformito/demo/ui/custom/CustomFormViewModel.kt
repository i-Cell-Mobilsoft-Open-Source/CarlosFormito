package com.icell.external.carlosformito.demo.ui.custom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.CarlosFormManager
import com.icell.external.carlosformito.core.api.FormManager
import kotlinx.coroutines.launch

class CustomFormViewModel :
    ViewModel(),
    FormManager by CarlosFormManager(CustomFormFields.build()) {

    init {
        viewModelScope.launch {
            initFormManager()
        }
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("CustomFormViewModel", "Form is valid")
            }
        }
    }
}
