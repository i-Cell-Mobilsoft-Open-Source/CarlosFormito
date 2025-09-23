package hu.icellmobilsoft.carlosformito.ui.datepicker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import hu.icellmobilsoft.carlosformito.ui.extension.toLocalDate
import kotlinx.datetime.LocalDate

object CarlosDatePickerDefaults {

    val ABSOLUTE_MIN_DATE = LocalDate(1900, 1, 1)
    val ABSOLUTE_MAX_DATE = LocalDate(2100, 1, 1)

    /**
     * Creates a [SelectableDates] implementation that restricts date selection within the provided range.
     *
     * @param minDate The minimum allowed date. If `null`, no lower bound is enforced.
     * @param maxDate The maximum allowed date. If `null`, no upper bound is enforced.
     *
     * @return A [SelectableDates] object that disables selection of dates outside
     * the given range. The comparison is inclusive (i.e., `minDate` and `maxDate` themselves are selectable).
     *
     * ### Example
     * ```kotlin
     * val datePickerState = rememberDatePickerState(
     *     selectableDates = CarlosDatePickerDefaults.selectableDates(
     *         minDate = LocalDate(2000, 1, 1),
     *         maxDate = LocalDate(2050, 12, 31)
     *     )
     * )
     * ```
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun selectableDates(
        minDate: LocalDate?,
        maxDate: LocalDate?
    ) = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val date = utcTimeMillis.toLocalDate()

            if (minDate != null && date < minDate) return false
            if (maxDate != null && date > maxDate) return false

            return true
        }
    }
}
