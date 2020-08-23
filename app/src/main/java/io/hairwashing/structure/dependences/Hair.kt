package io.hairwashing.structure.dependences

import io.hairwashing.tools.SetupFile.FileData.HairTypes
import io.hairwashing.tools.SetupFile.FileData.HairLengths
import io.hairwashing.tools.SetupFile.FileData.NEVER_WASHING
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.time.LocalDate

class Hair(var type: Type, var length: Length, var lastWashing: LocalDate) {

    enum class Type(val view: String) {
        DRY(HairTypes.DRY),
        REGULAR(HairTypes.REGULAR),
        OILY(HairTypes.OILY)
    }

    enum class Length(val view: String) {
        SHORT(HairLengths.SHORT),
        MIDDLE(HairLengths.MIDDLE),
        LONG(HairLengths.LONG)
    }

    companion object {
        private val NEVER_WASHING_DATE = LocalDate.now().minusDays(7)

        fun asDefault() = Hair(
            Type.REGULAR,
            Length.LONG,
            NEVER_WASHING_DATE
        )

        fun from(inputStream: FileInputStream) : Hair {
            val reader = BufferedReader(InputStreamReader(inputStream))
            val lines = reader.readLines()
            reader.close()
            val type = getTypeFromSetupLine(lines[0])
            val length = getLengthFromSetupLine(lines[1])
            val lastWashing = getLastWashingFromSetupLine(lines[2])
            return Hair(type, length, lastWashing)
        }

        private fun getTypeFromSetupLine(line: String) = when(getValueFromSetupLine(
            line
        )) {
            HairTypes.DRY -> Type.DRY
            HairTypes.REGULAR -> Type.REGULAR
            HairTypes.OILY -> Type.OILY
            else -> throw IllegalArgumentException("Unknown Hair.Type in line: $line")
        }

        private fun getLengthFromSetupLine(line: String) = when(getValueFromSetupLine(
            line
        )) {
            HairLengths.SHORT -> Length.SHORT
            HairLengths.MIDDLE -> Length.MIDDLE
            HairLengths.LONG -> Length.LONG
            else -> throw IllegalArgumentException("Unknown Hair.Length in line: $line")
        }

        private fun getLastWashingFromSetupLine(line: String) : LocalDate {
            val date =
                getValueFromSetupLine(
                    getValueFromSetupLine(
                        line
                    )
                )
            return if (date != NEVER_WASHING) {
                val (year, month, day) = date.split("-").map { it.toInt() }
                println(date)
                LocalDate.of(year, month, day)
            } else {
                NEVER_WASHING_DATE
            }
        }

        private fun getValueFromSetupLine(line: String) = line.split(":").last()
    }

    fun switchTypeToNext() {
        type = when(type) {
            Type.REGULAR -> Type.OILY
            Type.OILY -> Type.DRY
            else -> Type.REGULAR
        }
    }

    fun switchLengthToNext() {
        length = when(length) {
            Length.MIDDLE -> Length.LONG
            Length.LONG -> Length.SHORT
            else -> Length.MIDDLE
        }
    }

    fun getNextWashingDay() : LocalDate {
        val now = LocalDate.now()
        val next = getWashingDayAfter(lastWashing)
        if (next < now) return now
        return next
    }

    fun getWashingDayAfter(lastWashing: LocalDate) : LocalDate {
        return lastWashing.plusDays(getWashingBreakInDays().toLong())
    }

    private fun getWashingBreakInDays() : Int = when (length) {
        Length.SHORT -> when(type) {
            Type.DRY -> 5
            Type.REGULAR -> 3
            else -> 1
        }
        Length.MIDDLE -> when(type) {
            Type.DRY -> 6
            Type.REGULAR -> 3
            else -> 2
        }
        else -> when(type) {
            Type.DRY -> 7
            Type.REGULAR -> 4
            else -> 2
        }
    }
}