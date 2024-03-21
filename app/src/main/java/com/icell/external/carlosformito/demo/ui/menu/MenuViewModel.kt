package com.icell.external.carlosformito.demo.ui.menu

import androidx.lifecycle.ViewModel
import com.icell.external.carlosformito.core.FormManagerImpl
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy

const val KEY_VALIDATION_STRATEGY_FIELD = "KEY_VALIDATION_STRATEGY_FIELD"

class MenuViewModel :
    ViewModel(),
    FormManager by FormManagerImpl(
        formFields = listOf(
            FormField(
                id = KEY_VALIDATION_STRATEGY_FIELD,
                initialState = FormFieldState(FormFieldValidationStrategy.MANUAL),
            )
        )
    )
