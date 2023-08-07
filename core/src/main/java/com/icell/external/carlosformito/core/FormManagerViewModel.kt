package com.icell.external.carlosformito.core

import androidx.lifecycle.ViewModel
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy

abstract class FormManagerViewModel(
    formFields: List<FormField<*>>,
    validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.MANUAL
) : ViewModel(), FormManager by FormManagerImpl(formFields, validationStrategy)
