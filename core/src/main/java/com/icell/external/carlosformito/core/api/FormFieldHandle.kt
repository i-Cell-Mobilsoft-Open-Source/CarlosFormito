package com.icell.external.carlosformito.core.api

interface FormFieldHandle<T> {

    fun onFieldFocusCleared()

    fun <T> onFieldValueChanged(value: T?)

    fun setFieldHandleCallback(callback: FormFieldHandleCallback)
}
