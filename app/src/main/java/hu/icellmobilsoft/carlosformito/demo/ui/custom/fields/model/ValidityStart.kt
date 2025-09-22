package hu.icellmobilsoft.carlosformito.demo.ui.custom.fields.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class ValidityStart(
    val date: LocalDate? = null,
    val time: LocalTime? = null
) : Serializable {
    fun isFilled(): Boolean = date != null && time != null
}
