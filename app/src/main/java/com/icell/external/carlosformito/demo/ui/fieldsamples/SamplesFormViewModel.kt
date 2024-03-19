package com.icell.external.carlosformito.demo.ui.fieldsamples

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.FormManagerImpl
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

class SamplesFormViewModel(validationStrategy: FormFieldValidationStrategy) :
    ViewModel(), FormManager by FormManagerImpl(SamplesFormFields.build(), validationStrategy) {

    init {
        /**
         * This setup is needed for auto validation strategies
         */
        autoValidationScope = viewModelScope
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("SampleFormViewModel", "Form is valid")
            }
        }
    }
}
