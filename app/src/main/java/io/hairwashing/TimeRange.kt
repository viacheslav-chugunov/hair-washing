package io.hairwashing

import io.hairwashing.tools.SetupFile
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

enum class TimeRange(val days: Int, val view: String) {
    ONE_WEEK(7, SetupFile.FileData.TimeRanges.ONE_WEEK),
    TWO_WEEKS(14, SetupFile.FileData.TimeRanges.TWO_WEEKS),
    MONTH(30, SetupFile.FileData.TimeRanges.MONTH);

    companion object {
        fun from(inputStream: FileInputStream) : TimeRange {
            val reader = BufferedReader(InputStreamReader(inputStream))
            val lines = reader.readLines()
            reader.close()
            return when(getValueFromLine(lines[3])) {
                SetupFile.FileData.TimeRanges.ONE_WEEK -> ONE_WEEK
                SetupFile.FileData.TimeRanges.TWO_WEEKS -> TWO_WEEKS
                else -> MONTH
            }
        }

        private fun getValueFromLine(line: String) = line.split(":").last()
    }

    fun getNext() = when(this) {
        ONE_WEEK -> TWO_WEEKS
        TWO_WEEKS -> MONTH
        else -> ONE_WEEK
    }
}