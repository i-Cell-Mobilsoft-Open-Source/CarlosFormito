package com.icell.external.carlosformito.demo.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.core.CarlosFormManager
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
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
