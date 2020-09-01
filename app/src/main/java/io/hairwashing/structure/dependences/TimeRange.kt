package io.hairwashing.structure.dependences

import android.content.Context
import io.hairwashing.db.ConfigDB
import io.hairwashing.tools.TimeRanges

enum class TimeRange(val days: Int, val view: String) {
    ONE_WEEK(7, TimeRanges.ONE_WEEK),
    TWO_WEEKS(14, TimeRanges.TWO_WEEKS),
    MONTH(30, TimeRanges.MONTH);

    companion object {

        fun fromDB(context: Context) : TimeRange {
            val db = ConfigDB(context)
            val values = ConfigDB.Companion
            val timeRange = getTimeRangeBy(db.getArgBy(values.VALUE_TIME_RANGE))
            db.close()
            return timeRange
        }

        private fun getTimeRangeBy(arg: String) = when(arg) {
            TimeRanges.ONE_WEEK -> ONE_WEEK
            TimeRanges.TWO_WEEKS -> TWO_WEEKS
            else -> MONTH
        }
    }

    fun getNext() = when(this) {
        ONE_WEEK -> TWO_WEEKS
        TWO_WEEKS -> MONTH
        else -> ONE_WEEK
    }
}