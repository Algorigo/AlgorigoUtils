package com.algorigo.library

import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.PeriodFormatterBuilder
import java.util.Locale

/**
 * Created by jaehongyoo on 2018. 3. 9..
 */
object DateTimeUtil {

    enum class DayOfWeek(val value: Int) {
        MONDAY(1),
        TUESDAY(2),
        WEDNESDAY(3),
        THURSDAY(4),
        FRIDAY(5),
        SATURDAY(6),
        SUNDAY(7)
    }

    fun getDateTimeStringWithPattern(dateTime: DateTime, pattern: String): String {
        val dateTimeFormat = DateTimeFormat.forPattern(pattern)
        val str = dateTimeFormat.print(dateTime)
        return str
    }

    fun getMediumDateTimeString(dateTime: DateTime, locale: Locale = Locale.getDefault()): String {
        val dateTimeFormat = DateTimeFormat.mediumDateTime().withLocale(locale)
        val str = dateTime.toString(dateTimeFormat)
        return str
    }

    fun getShortDateTimeString(dateTime: DateTime, locale: Locale = Locale.getDefault()): String {
        val dateTimeFormat = DateTimeFormat.shortDateTime().withLocale(locale)
        val str = dateTime.toString(dateTimeFormat)
        return str
    }

    fun getMediumDateString(dateTime: DateTime, locale: Locale = Locale.getDefault()): String {
        return getMediumDateString(dateTime.toLocalDate(), locale)
    }

    fun getMediumDateString(localDate: LocalDate, locale: Locale = Locale.getDefault()): String {
        val dateFormat = DateTimeFormat.mediumDate().withLocale(locale)
        val str = localDate.toString(dateFormat)
        return str
    }

    fun getShortDateString(dateTime: DateTime, locale: Locale = Locale.getDefault()): String {
        return getShortDateString(dateTime.toLocalDate(), locale)
    }

    fun getShortDateString(localDate: LocalDate, locale: Locale = Locale.getDefault()): String {
        val dateFormat = DateTimeFormat.shortDate().withLocale(locale)
        val str = localDate.toString(dateFormat)
        return str
    }

    fun getMediumTimeString(dateTime: DateTime, locale: Locale = Locale.getDefault()): String {
        return getMediumTimeString(dateTime.toLocalTime(), locale)
    }

    fun getMediumTimeString(localTime: LocalTime, locale: Locale = Locale.getDefault()): String {
        val timeFormat = DateTimeFormat.mediumTime().withLocale(locale)
        val str = localTime.toString(timeFormat)
        return str
    }

    fun getShortTimeString(dateTime: DateTime, locale: Locale = Locale.getDefault()): String {
        return getShortTimeString(dateTime.toLocalTime(), locale)
    }

    fun getShortTimeString(localTime: LocalTime, locale: Locale = Locale.getDefault()): String {
        val timeFormat = DateTimeFormat.shortTime().withLocale(locale)
        val str = localTime.toString(timeFormat)
        return str
    }

    fun getLastDayOfMonth(dateTime: DateTime): LocalDate {
        val lastDayOfMonth = dateTime.dayOfMonth().withMaximumValue()
        return lastDayOfMonth.toLocalDate()
    }

    fun getFirstDayOfWeek(dateTime: DateTime, firstDay: DayOfWeek = DayOfWeek.SUNDAY): LocalDate {
        var firstDayOfWeek = dateTime.withDayOfWeek(firstDay.value)
        if (firstDayOfWeek.isAfter(dateTime)) {
            firstDayOfWeek = firstDayOfWeek.minusDays(7)
        }
        return firstDayOfWeek.toLocalDate()
    }

    fun getLastDayOfWeek(dateTime: DateTime, firstDay: DayOfWeek = DayOfWeek.SUNDAY): LocalDate {
        var weekNum = if (firstDay == DayOfWeek.MONDAY) DayOfWeek.SUNDAY.value else firstDay.value-1
        var lastDayOfWeek = dateTime.withDayOfWeek(weekNum)
        if (lastDayOfWeek.isBefore(dateTime)) {
            lastDayOfWeek = lastDayOfWeek.plusDays(7)
        }
        return lastDayOfWeek.toLocalDate()
    }

    fun getHourMinutePeriodString(period: Period): String {
        val periodFormatter = PeriodFormatterBuilder()
                .printZeroAlways()
                .minimumPrintedDigits(1)
                .appendHours()
                .appendSeparator(":")
                .minimumPrintedDigits(2)
                .appendMinutes()
                .toFormatter()
        val str = periodFormatter.print(period)
        return str
    }

    fun getDateTimeWithPattern(strDate: String, pattern: String): DateTime {
        val dateTimeFormat = DateTimeFormat.forPattern(pattern)
        val dateTime = dateTimeFormat.parseDateTime(strDate)
        return dateTime
    }

    fun getDaysBetweenDateTimes(from: DateTime, to: DateTime): Int {
        return Days.daysBetween(from, to).days
    }
}