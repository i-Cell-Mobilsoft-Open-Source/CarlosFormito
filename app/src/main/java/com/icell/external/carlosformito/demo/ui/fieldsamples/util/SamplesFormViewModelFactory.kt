package com.icell.external.carlosformito.demo.ui.fieldsamples.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.demo.ui.fieldsamples.SamplesFormViewModel

@Suppress("UNCHECKED_CAST")
class SamplesFormViewModelFactory(
    private val validationStrategy: FormFieldValidationStrategy
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SamplesFormViewModel(validationStrategy) as T
    }
}
