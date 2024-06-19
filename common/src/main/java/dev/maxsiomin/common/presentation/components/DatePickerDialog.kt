package dev.maxsiomin.common.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.*

@Composable
fun DatePickerDialog(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val currentYear = date.year
        val currentMonth = date.monthNumber - 1  // DatePickerDialog uses 0-based month index
        val currentDay = date.dayOfMonth

        android.app.DatePickerDialog(context, { _, year, month, dayOfMonth ->
            // Update the date when a new date is selected
            val newDate = LocalDate(year, month + 1, dayOfMonth)
            onDateChange(newDate)
        }, currentYear, currentMonth, currentDay).show()
    }

}