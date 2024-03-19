package com.icell.external.carlosformito.material3demo.ui.fieldsamples

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy

@Suppress("UNCHECKED_CAST")
class SamplesFormViewModelFactory(
    private val validationStrategy: FormFieldValidationStrategy
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SamplesFormViewModel(validationStrategy) as T
    }
}
