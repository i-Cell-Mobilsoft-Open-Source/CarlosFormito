package hu.icellmobilsoft.carlosformito.demo.ui.custom.fields.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.io.Serializable

data class ValidityStart(
    val date: LocalDate? = null,
    val time: LocalTime? = null
) : Serializable {
    fun isFilled(): Boolean = date != null && time != null
}
