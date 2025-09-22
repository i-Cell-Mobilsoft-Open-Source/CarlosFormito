package hu.icellmobilsoft.carlosformito.core.api.validator

/**
 * Interface representing the context of form field values.
 *
 * This interface provides a method to retrieve the value of a form field by its ID.
 * It can be used in validation scenarios where the value of one field depends on the
 * values of other fields.
 */
interface FormValueContext {

    /**
     * Retrieves the value of the form field with the given ID.
     *
     * @param T The type of the form field value.
     * @param id The identifier of the form field.
     * @return The value of the form field, or null if the field does not exist or has no value.
     */
    fun <T> getFieldValue(id: String): T?
}
