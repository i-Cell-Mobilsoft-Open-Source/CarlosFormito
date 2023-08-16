package com.icell.external.carlosformito.ui.custom

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.FormManagerViewModel
import kotlinx.coroutines.launch

class CustomFormViewModel : FormManagerViewModel(CustomFormFields.build()) {

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("CustomFormViewModel", "Form is valid")
            }
        }
    }
}
