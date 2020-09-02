package io.hairwashing.structure.dependences

import android.content.Context
import io.hairwashing.db.ConfigDB
import java.time.LocalDate

class Hair private constructor(var type: Type, var length: Length, var lastWashing: LocalDate) {

    enum class Type(val view: String) {
        DRY("dry"),
        REGULAR("regular"),
        OILY("oily")
    }

    enum class Length(val view: String) {
        SHORT("short"),
        MIDDLE("middle"),
        LONG("long")
    }

    companion object {
        val NEVER_WASHING_DATE: LocalDate = LocalDate.now().minusDays(7)

        fun asDefault() = Hair(Type.REGULAR, Length.LONG, NEVER_WASHING_DATE)

        fun fromDB(context: Context) : Hair {
            val db = ConfigDB(context)
            val v = ConfigDB.Companion
            val type = getTypeBy(db.getArgBy(v.VALUE_HAIR_TYPE))
            val length = getLengthBy(db.getArgBy(v.VALUE_HAIR_LENGTH))
            val lastWashing = getLastWashingBy(db.getArgBy(v.VALUE_LAST_WASHING))
            db.close()
            return Hair(type, length, lastWashing)
        }

        private fun getTypeBy(arg: String) = when(arg) {
            Type.DRY.view -> Type.DRY
            Type.REGULAR.view -> Type.REGULAR
            Type.OILY.view -> Type.OILY
            else -> throw IllegalArgumentException("Unknown Hair.Type in $arg")
        }

        private fun getLengthBy(arg: String) = when(arg) {
            Length.SHORT.view -> Length.SHORT
            Length.MIDDLE.view -> Length.MIDDLE
            Length.LONG.view -> Length.LONG
            else -> throw IllegalArgumentException("Unknown Hair.Length in $arg")
        }

        private fun getLastWashingBy(arg: String) : LocalDate {
            val (year, month, day) = arg.split("-").map { it.toInt() }
            return LocalDate.of(year, month, day)
        }
    }

    fun switchTypeToNext() {
        type = when(type) {
            Type.REGULAR -> Type.OILY
            Type.OILY -> Type.DRY
            Type.DRY -> Type.REGULAR
        }
    }

    fun switchLengthToNext() {
        length = when(length) {
            Length.MIDDLE -> Length.LONG
            Length.LONG -> Length.SHORT
            Length.SHORT -> Length.MIDDLE
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

    private fun getWashingBreakInDays() : Int {
        val typeWeight = when(type) {
            Type.DRY -> 7
            Type.REGULAR -> 4
            Type.OILY -> 3
        }
        val lengthWeight = when(length) {
            Length.SHORT -> -2
            Length.MIDDLE -> -1
            Length.LONG -> 0
        }
        var breakInDays = typeWeight + lengthWeight
        if (breakInDays < 1)
            breakInDays = 1
        return breakInDays
    }
}