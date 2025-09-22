package hu.icellmobilsoft.carlosformito.demo.ui.password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.icellmobilsoft.carlosformito.core.CarlosFormManager
import hu.icellmobilsoft.carlosformito.core.api.FormManager
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

class UpdatePasswordViewModel(
    validationStrategy: FormFieldValidationStrategy,
) : ViewModel(), FormManager by CarlosFormManager(UpdatePasswordFields.build(), validationStrategy) {

    init {
        viewModelScope.launch {
            initFormManager()
        }
    }

    fun submit() {
        viewModelScope.launch {
            if (validateForm()) {
                Log.i("UpdatePasswordViewModel", "Form is valid")
            }
        }
    }
}
