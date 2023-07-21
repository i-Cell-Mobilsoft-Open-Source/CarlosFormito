package com.icell.external.carlosformito.ui.fieldsamples

import android.util.Log
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.core.FormManagerViewModel

class SamplesFormViewModel : FormManagerViewModel(
    formFields = SamplesFormItems.buildItems(),
    validationStrategy = FormFieldValidationStrategy.ON_FOCUS_CLEAR
) {
    fun submit() {
        if (validateForm()) {
            Log.i("SampleFormViewModel", "Form is valid")
        }
    }
}
