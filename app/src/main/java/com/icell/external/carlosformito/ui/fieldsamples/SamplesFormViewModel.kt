package com.icell.external.carlosformito.ui.fieldsamples

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.FormManagerViewModel
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

class SamplesFormViewModel : FormManagerViewModel(
    formFields = SamplesFormFields.build(),
    validationStrategy = FormFieldValidationStrategy.ON_FOCUS_CLEAR
) {
    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("SampleFormViewModel", "Form is valid")
            }
        }
    }
}
