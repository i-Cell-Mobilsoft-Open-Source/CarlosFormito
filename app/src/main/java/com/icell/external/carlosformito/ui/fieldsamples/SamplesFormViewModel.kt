package com.icell.external.carlosformito.ui.fieldsamples

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.FormManagerViewModel
import com.icell.external.carlosformito.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

class SamplesFormViewModel(
    validationStrategy: FormFieldValidationStrategy
) : FormManagerViewModel(
    formFields = SamplesFormFields.build(),
    validationStrategy = validationStrategy
) {
    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("SampleFormViewModel", "Form is valid")
            }
        }
    }
}
