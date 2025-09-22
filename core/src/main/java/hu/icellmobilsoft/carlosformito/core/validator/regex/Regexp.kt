package hu.icellmobilsoft.carlosformito.core.validator.regex

/**
 * `Regexp` object contains regular expressions commonly used for form field validations.
 */
object Regexp {
    /**
     * Regular expression pattern for validating email addresses.
     */
    const val EMAIL_REGEXP = "[A-Za-z0-9_%+-][A-Za-z0-9._%+-]*@([A-Za-z0-9_-]+\\.)+[A-Za-z]{2,4}"
}
