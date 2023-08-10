package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import java.util.Locale

class ContainsSpecialCharacterValidator(
    @StringRes private val errorMessageId: Int
) : FormFieldValidator<String> {

    override suspend fun validate(value: String?): FormFieldValidationResult {
        return if (value.isNullOrBlank() || !containsSpecialChars(value)) {
            FormFieldValidationResult.Invalid.Message(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }

    private fun containsSpecialChars(nonBlankInput: String): Boolean {
        val inputWithoutSpaces = nonBlankInput.replace("\\s".toRegex(), "")

        val numberSet = NUMBERS.toSet()
        val lowercaseSet = LETTERS.toSet()
        val uppercaseSet = LETTERS.uppercase(Locale.getDefault()).toSet()

        val notSpecialChars = numberSet + lowercaseSet + uppercaseSet
        val inputChars = inputWithoutSpaces.toCharArray().toSet()

        val specialChars = inputChars.subtract(notSpecialChars)
        return specialChars.isNotEmpty()
    }

    companion object {
        const val NUMBERS = "0123456789"
        const val LETTERS = "abcdefghijklmnopqrstuvwxyz"
    }
}
