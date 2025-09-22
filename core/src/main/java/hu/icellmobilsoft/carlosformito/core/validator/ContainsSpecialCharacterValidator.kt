package hu.icellmobilsoft.carlosformito.core.validator

import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import java.util.Locale

/**
 * `ContainsSpecialCharacterValidator` validates that a String value contains at least one special character.
 *
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class ContainsSpecialCharacterValidator(
    private val errorMessageId: Int? = null
) : FormFieldValidator<String> {

    /**
     * Validates the given String value.
     *
     * @param value The String value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (contains at least one special character),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: String?): FormFieldValidationResult {
        val nonNullValue = value.orEmpty()
        if (nonNullValue.isEmpty()) {
            return FormFieldValidationResult.Valid
        }
        return if (!containsSpecialChars(nonNullValue)) {
            FormFieldValidationResult.Invalid.of(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }

    /**
     * Checks if the input String contains any special characters.
     *
     * @param value The String value to check.
     * @return `true` if the input String contains at least one special character, `false` otherwise.
     */
    private fun containsSpecialChars(value: String): Boolean {
        if (value.isBlank()) return false

        val inputWithoutSpaces = value.replace("\\s".toRegex(), "")

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
