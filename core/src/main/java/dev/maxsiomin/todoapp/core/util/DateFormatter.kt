package dev.maxsiomin.todoapp.core.util

import android.content.Context
import dev.maxsiomin.common.extensions.toLocalDate
import dev.maxsiomin.todoapp.core.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import javax.inject.Inject

abstract class DateFormatter(private val context: Context) {
    abstract fun formatDate(date: LocalDate): String

    // Getter set so that if language is updated, monthNames had relevant string values
    protected val monthNames
        get() = MonthNames(
            january = context.getString(R.string.january),
            february = context.getString(R.string.february),
            march = context.getString(R.string.march),
            april = context.getString(R.string.april),
            may = context.getString(R.string.may),
            june = context.getString(R.string.june),
            july = context.getString(R.string.july),
            august = context.getString(R.string.august),
            september = context.getString(R.string.september),
            october = context.getString(R.string.october),
            november = context.getString(R.string.november),
            december = context.getString(R.string.december),
        )
}

class DefaultDateFormatter @Inject constructor(context: Context) : DateFormatter(context) {

    override fun formatDate(date: LocalDate): String {
        val dateFormat = LocalDate.Format {
            monthName(names = monthNames)
            chars(" ")
            dayOfMonth(Padding.NONE)
            chars(", ")
            year()
        }
        return dateFormat.format(date)
    }

}

class RussianDateFormatter @Inject constructor(context: Context) : DateFormatter(context) {

    override fun formatDate(date: LocalDate): String {
        val dateFormat = LocalDate.Format {
            dayOfMonth(Padding.NONE)
            chars(" ")
            monthName(names = monthNames)
            chars(", ")
            year()
        }
        return dateFormat.format(date)
    }

}