package io.hairwashing.dependences

import android.content.Context
import io.hairwashing.db.ConfigDB

enum class TimeRange(val days: Int, val view: String) {
    ONE_WEEK(7, "one_week"),
    TWO_WEEKS(14, "two_weeks"),
    MONTH(30, "month");

    companion object {

        fun fromDB(context: Context) : TimeRange {
            val db = ConfigDB(context)
            val values = ConfigDB.Companion
            val timeRange = getTimeRangeBy(db.getArgBy(values.VALUE_TIME_RANGE))
            db.close()
            return timeRange
        }

            private fun getTimeRangeBy(arg: String) = when(arg) {
                ONE_WEEK.view -> ONE_WEEK
                TWO_WEEKS.view -> TWO_WEEKS
                else -> MONTH
            }
    }

    fun getNext() = when(this) {
        ONE_WEEK -> TWO_WEEKS
        TWO_WEEKS -> MONTH
        else -> ONE_WEEK
    }
}