package com.icell.external.carlosformito.demo.ui.custom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.FormManagerImpl
import com.icell.external.carlosformito.core.api.FormManager
import kotlinx.coroutines.launch

class CustomFormViewModel :
    ViewModel(),
    FormManager by FormManagerImpl(CustomFormFields.build()) {

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("CustomFormViewModel", "Form is valid")
            }
        }
    }
}
