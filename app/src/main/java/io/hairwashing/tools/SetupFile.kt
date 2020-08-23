package io.hairwashing.tools

import io.hairwashing.TimeRange
import io.hairwashing.structure.dependences.Hair
import io.hairwashing.tools.adapter.WeeklyAdapter
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter

object SetupFile {

    object FileData {
        const val  FILE_NAME = "setup"

        object Columns {
            const val HAIR_TYPE = "hair_type"
            const val HAIR_LENGTH = "hair_length"
            const val LAST_WASHING = "last_washing"
            const val TIME_RANGE = "time_range"
        }

        object HairTypes {
            const val DRY = "dry"
            const val REGULAR = "regular"
            const val OILY = "oily"
        }

        object HairLengths {
            const val SHORT = "short"
            const val MIDDLE = "middle"
            const val LONG = "long"
        }

        object TimeRanges {
            const val ONE_WEEK = "one_week"
            const val TWO_WEEKS = "two_weeks"
            const val MONTH = "month"
        }

        const val NEVER_WASHING = "never"
    }

    fun initFrom(outputStream: FileOutputStream) {
        updateValuesFromStream(outputStream, Hair.asDefault(), TimeRange.ONE_WEEK)
    }

    fun existsIn(files: Array<String>) : Boolean {
        for (file in files)
            if (file == FileData.FILE_NAME)
                return true
        return false
    }

    fun updateValuesFromStream(outputStream: FileOutputStream, hair: Hair,
                               timeRange: TimeRange) {
        val writer = BufferedWriter(OutputStreamWriter(outputStream))
        val arg = FileData.Columns
        val formattedHairType = "${arg.HAIR_TYPE}:${hair.type.view}\n"
        val formattedHairLength = "${arg.HAIR_LENGTH}:${hair.length.view}\n"
        val formattedLastWashing = "${arg.LAST_WASHING}:${hair.lastWashing}\n"
        val formattedTimeRange = "${arg.TIME_RANGE}:${timeRange.view}\n"
        writer.append(formattedHairType + formattedHairLength + formattedLastWashing + formattedTimeRange)
        writer.close()
    }
}