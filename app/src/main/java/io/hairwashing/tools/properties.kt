package io.hairwashing.tools

import io.hairwashing.TimeRange
import io.hairwashing.structure.dependences.Hair
import io.hairwashing.tools.adapter.WeeklyAdapter
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter

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