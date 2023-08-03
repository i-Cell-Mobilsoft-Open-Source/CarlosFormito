package com.icell.external.carlosformito.ui.custom

import android.util.Log
import com.icell.external.carlosformito.core.FormManagerViewModel

class CustomFormViewModel : FormManagerViewModel(CustomFormFields.buildItems()) {

    fun submit() {
        if (validateForm()) {
            Log.i("CustomFormViewModel", "Form is valid")
        }
    }
}
