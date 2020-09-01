package io.hairwashing.structure.dependences

import android.content.Context
import io.hairwashing.db.ConfigDB
import io.hairwashing.tools.HairLengths
import io.hairwashing.tools.HairTypes
import io.hairwashing.tools.NEVER_WASHING
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
            HairTypes.DRY -> Type.DRY
            HairTypes.REGULAR -> Type.REGULAR
            HairTypes.OILY -> Type.OILY
            else -> throw IllegalArgumentException("Unknown Hair.Type in $arg")
        }

        private fun getLengthBy(arg: String) = when(arg) {
            HairLengths.SHORT -> Length.SHORT
            HairLengths.MIDDLE -> Length.MIDDLE
            HairLengths.LONG -> Length.LONG
            else -> throw IllegalArgumentException("Unknown Hair.Length in $arg")
        }

        private fun getLastWashingBy(arg: String) : LocalDate {
            return if (arg != NEVER_WASHING) {
                val (year, month, day) = arg.split("-").map { it.toInt() }
                LocalDate.of(year, month, day)
            } else {
                NEVER_WASHING_DATE
            }
        }
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