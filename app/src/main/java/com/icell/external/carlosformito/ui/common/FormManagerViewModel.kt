package com.icell.external.carlosformito.ui.common

import androidx.lifecycle.ViewModel
import com.icell.external.carlosformito.FormManagerImpl
import com.icell.external.carlosformito.api.FormManager
import com.icell.external.carlosformito.api.model.FormField
import com.icell.external.carlosformito.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.CoroutineExceptionHandler

open class FormManagerViewModel(
    formFields: List<FormField<*>>,
    validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.MANUAL,
    validationExceptionHandler: CoroutineExceptionHandler? = null
) : ViewModel(),
    FormManager by FormManagerImpl(formFields, validationStrategy, validationExceptionHandler)
