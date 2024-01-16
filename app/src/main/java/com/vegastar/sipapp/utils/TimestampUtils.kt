package com.vegastar.sipapp.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TimestampUtils {
    companion object {
        fun isToday(timestamp: Long, timestampInSecs: Boolean = true): Boolean {
            val cal = Calendar.getInstance()
            cal.timeInMillis = if (timestampInSecs) timestamp * 1000 else timestamp
            return isSameDay(cal, Calendar.getInstance())
        }

        fun isYesterday(timestamp: Long, timestampInSecs: Boolean = true): Boolean {
            val yesterday = Calendar.getInstance()
            yesterday.roll(Calendar.DAY_OF_MONTH, -1)
            val cal = Calendar.getInstance()
            cal.timeInMillis = if (timestampInSecs) timestamp * 1000 else timestamp
            return isSameDay(cal, yesterday)
        }

        fun isSameDay(timestamp1: Long, timestamp2: Long, timestampInSecs: Boolean = true): Boolean {
            val cal1 = Calendar.getInstance()
            cal1.timeInMillis = if (timestampInSecs) timestamp1 * 1000 else timestamp1
            val cal2 = Calendar.getInstance()
            cal2.timeInMillis = if (timestampInSecs) timestamp2 * 1000 else timestamp2
            return isSameDay(cal1, cal2)
        }

        fun isSameDay(cal1: Date, cal2: Date): Boolean {
            return isSameDay(cal1.time, cal2.time, false)
        }

        fun durationToString(hours: Int, minutes: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hours)
            calendar.set(Calendar.MINUTE, minutes)
            val pattern = when {
                hours == 0 -> "mm'min'"
                hours < 10 && minutes == 0 -> "H'h'"
                hours < 10 && minutes > 0 -> "H'h'mm"
                hours >= 10 && minutes == 0 -> "HH'h'"
                else -> "HH'h'mm"
            }
            return SimpleDateFormat(pattern, Locale.getDefault()).format(calendar.time)
        }

        private fun isSameYear(timestamp: Long, timestampInSecs: Boolean = true): Boolean {
            val cal = Calendar.getInstance()
            cal.timeInMillis = if (timestampInSecs) timestamp * 1000 else timestamp
            return isSameYear(cal, Calendar.getInstance())
        }

        fun toString(timestamp: Long, onlyDate: Boolean = false, timestampInSecs: Boolean = true, shortDate: Boolean = true, hideYear: Boolean = true): String {
            val dateFormat = if (isToday(timestamp, timestampInSecs)) {
                DateFormat.getTimeInstance(DateFormat.SHORT)
            } else {
                if (onlyDate) {
                    DateFormat.getDateInstance(if (shortDate) DateFormat.SHORT else DateFormat.FULL)
                } else {
                    DateFormat.getDateTimeInstance(if (shortDate) DateFormat.SHORT else DateFormat.MEDIUM, DateFormat.SHORT
                    )
                }
            } as SimpleDateFormat

            if (hideYear || isSameYear(timestamp, timestampInSecs)) {
                // Remove the year part of the format
                dateFormat.applyPattern(dateFormat.toPattern()
                    .replace("/?y+/?|,?\\s?y+\\s?".toRegex(), if (shortDate) "" else " "
                    )
                )
            }

            val millis = if (timestampInSecs) timestamp * 1000 else timestamp
            return dateFormat.format(Date(millis)).capitalize(Locale.getDefault())
        }

        private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
            return cal1[Calendar.ERA] == cal2[Calendar.ERA] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
        }

        private fun isSameYear(cal1: Calendar, cal2: Calendar): Boolean {
            return cal1[Calendar.ERA] == cal2[Calendar.ERA] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
        }

        fun formatTimestamp(timestamp: Long): String {
            val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            val millis = timestamp * 1000 // Chuyển đổi sang milliseconds nếu timestamp được cung cấp dưới dạng giây
            return dateFormat.format(Date(millis)).capitalize(Locale.getDefault())
        }
    }
}