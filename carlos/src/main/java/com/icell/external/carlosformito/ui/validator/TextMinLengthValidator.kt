package com.icell.external.carlosformito.ui.validator

import androidx.annotation.IntRange
import androidx.annotation.StringRes
import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.api.validator.FormFieldValidator
import com.icell.external.carlosformito.api.validator.RequiresFieldValue

class TextMinLengthValidator(
    @IntRange(from = MIN_LENGTH_RANGE_FROM)
    private val minLength: Int,
    @StringRes
    private val errorMessageId: Int = R.string.carlos_lbl_validator_min_length
) : FormFieldValidator<String>, RequiresFieldValue {

    init {
        check(minLength >= MIN_LENGTH_RANGE_FROM) {
            "minLength param must be greater than or equal to $MIN_LENGTH_RANGE_FROM"
        }
    }

    override suspend fun validate(value: String?): FormFieldValidationResult {
        return if ((value ?: "").length < minLength) {
            FormFieldValidationResult.Invalid.MessageWithArgs(
                errorMessageId,
                listOf(minLength)
            )
        } else {
            FormFieldValidationResult.Valid
        }
    }

    companion object {
        private const val MIN_LENGTH_RANGE_FROM = 1L
    }
}
