package hu.icellmobilsoft.carlosformito.demo.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.icellmobilsoft.carlosformito.core.CarlosFormManager
import hu.icellmobilsoft.carlosformito.core.api.FormManager
import hu.icellmobilsoft.carlosformito.core.api.model.FormField
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.launch

const val KEY_VALIDATION_STRATEGY_FIELD = "KEY_VALIDATION_STRATEGY_FIELD"

class MenuViewModel :
    ViewModel(),
    FormManager by CarlosFormManager(
        formFields = listOf(
            FormField(
                id = KEY_VALIDATION_STRATEGY_FIELD,
                initialValue = FormFieldValidationStrategy.Manual,
            )
        )
    ) {

    init {
        viewModelScope.launch {
            initFormManager()
        }
    }
}
