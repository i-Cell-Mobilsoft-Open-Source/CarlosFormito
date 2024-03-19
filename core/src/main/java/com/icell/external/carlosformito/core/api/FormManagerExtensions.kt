package com.icell.external.carlosformito.core.api

fun <T> FormManager.getFieldValue(id: String): T? = getFieldItem<T>(id).fieldState.value.value

fun <T> FormManager.requireFieldValue(id: String): T = requireNotNull(getFieldValue(id))
