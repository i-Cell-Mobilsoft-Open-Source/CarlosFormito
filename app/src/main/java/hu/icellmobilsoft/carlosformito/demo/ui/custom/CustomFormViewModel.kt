package hu.icellmobilsoft.carlosformito.demo.ui.custom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.icellmobilsoft.carlosformito.core.CarlosFormManager
import hu.icellmobilsoft.carlosformito.core.api.FormManager
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

class CustomFormViewModel(
    validationStrategy: FormFieldValidationStrategy
) : ViewModel(), FormManager by CarlosFormManager(CustomFormFields.build(), validationStrategy) {

    init {
        viewModelScope.launch {
            initFormManager()
        }
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("CustomFormViewModel", "Form is valid")
            }
        }
    }
}
