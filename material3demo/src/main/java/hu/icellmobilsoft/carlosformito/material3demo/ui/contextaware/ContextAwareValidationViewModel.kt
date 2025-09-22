package hu.icellmobilsoft.carlosformito.material3demo.ui.contextaware

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.icellmobilsoft.carlosformito.core.CarlosFormManager
import hu.icellmobilsoft.carlosformito.core.api.FormManager
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

class ContextAwareValidationViewModel(validationStrategy: FormFieldValidationStrategy) :
    ViewModel(), FormManager by CarlosFormManager(ContextAwareFormFields.build(), validationStrategy) {

    init {
        viewModelScope.launch {
            initFormManager()
        }
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                println("ContextAwareValidationViewModel - Form is valid")
            }
        }
    }
}
