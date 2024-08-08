package com.icell.external.carlosformito.ui.extension

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity

/**
 * Extension function for [Context] that retrieves the current [AppCompatActivity].
 *
 * This function traverses the context wrappers until it finds an instance of [AppCompatActivity].
 * If no [AppCompatActivity] is found, it throws an [IllegalStateException].
 *
 * @return The current [AppCompatActivity].
 * @throws IllegalStateException If no [AppCompatActivity] is found in the context chain.
 */
fun Context.requireActivity(): AppCompatActivity {
    var context = this
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    error("This function should be called only in the context of an Activity.")
}
