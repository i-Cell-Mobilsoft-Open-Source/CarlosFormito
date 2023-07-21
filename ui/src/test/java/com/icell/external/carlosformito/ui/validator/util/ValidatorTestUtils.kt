package com.icell.external.carlosformito.ui.validator.util

import com.google.common.truth.Subject
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult

object ValidatorTestUtils {

    fun Subject.isValidationResultInvalid() =
        isInstanceOf(FormFieldValidationResult.Invalid::class.java)
}
