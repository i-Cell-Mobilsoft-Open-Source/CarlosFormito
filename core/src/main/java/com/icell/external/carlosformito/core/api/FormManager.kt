package com.icell.external.carlosformito.core.api

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface representing a form manager, responsible for managing form fields and validation.
 */
interface FormManager : FormFieldItemListener {

    /**
     * State flow indicating whether all required fields are filled.
     */
    val allRequiredFieldFilled: StateFlow<Boolean>

    /**
     * State flow indicating whether validation is in progress.
     */
    val validationInProgress: StateFlow<Boolean>

    /**
     * Initializes the form manager.
     *
     * @param autoValidationExceptionHandler Optional coroutine exception handler for auto-validation.
     */
    suspend fun initFormManager(autoValidationExceptionHandler: CoroutineExceptionHandler? = null)

    /**
     * Retrieves the form field item associated with the given ID.
     *
     * @param id The unique identifier of the form field item.
     * @return The form field item associated with the ID.
     */
    fun <T> getFieldItem(id: String): FormFieldItem<T>

    /**
     * Validates the entire form.
     *
     * @return `true` if the form is valid after validation, `false` otherwise.
     */
    suspend fun validateForm(): Boolean

    /**
     * Marks the entire form as invalid.
     */
    fun setFormInvalid()

    /**
     * Clears the entire form, resetting all fields to their initial states.
     */
    fun clearForm()

    /**
     * Prints the current state of the form for debugging purposes.
     */
    fun printFormState()
}
