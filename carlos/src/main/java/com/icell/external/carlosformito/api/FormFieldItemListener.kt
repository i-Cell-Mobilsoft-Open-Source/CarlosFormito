package com.icell.external.carlosformito.api

interface FormFieldItemListener {

    fun onFieldFocusCleared(id: String)

    fun <T> onFieldValueChanged(id: String, value: T?)
}
