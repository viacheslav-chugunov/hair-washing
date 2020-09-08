package io.hairwashing.dependences

import android.content.Context
import io.hairwashing.db.ConfigDB
import java.time.LocalDate

class Hair private constructor(var type: Type, var length: Length, var climate: Climate,
                               var lastWashing: LocalDate, var preLastWashing: LocalDate) {

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

    enum class Climate(val view: String) {
        FRIGID("frigid"),
        SIMPLE("simple"),
        HOT("hot")
    }

    companion object {
        val NEVER_WASHING_DATE: LocalDate = LocalDate.now().minusDays(7)

        fun asDefault() = Hair(Type.REGULAR, Length.LONG, Climate.FRIGID, NEVER_WASHING_DATE, NEVER_WASHING_DATE)

        fun fromDB(context: Context) : Hair {
            val db = ConfigDB(context)
            val v = ConfigDB.Companion
            val type = getTypeBy(db.getArgBy(v.VALUE_HAIR_TYPE))
            val length = getLengthBy(db.getArgBy(v.VALUE_HAIR_LENGTH))
            val climate = getClimateBy(db.getArgBy(v.VALUE_CLIMATE))
            val lastWashing = getWashingDateBy(db.getArgBy(v.VALUE_LAST_WASHING))
            val preLastWashing = getWashingDateBy(db.getArgBy(v.VALUE_PRE_LAST_WASHING))
            db.close()
            return Hair(type, length, climate, lastWashing, preLastWashing)
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

            private fun getClimateBy(arg: String) = when(arg) {
                Climate.FRIGID.view -> Climate.FRIGID
                Climate.SIMPLE.view -> Climate.SIMPLE
                Climate.HOT.view -> Climate.HOT
                else -> throw IllegalArgumentException("Unknown Hair.Climate in $arg")
            }

            private fun getWashingDateBy(arg: String) : LocalDate {
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

    fun switchClimateToNext() {
        climate = when(climate) {
            Climate.FRIGID -> Climate.SIMPLE
            Climate.SIMPLE -> Climate.HOT
            Climate.HOT -> Climate.FRIGID
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
        val climateWeight = when(climate) {
            Climate.FRIGID -> 1
            Climate.SIMPLE -> 0
            Climate.HOT -> -1
        }
        var breakInDays = typeWeight + lengthWeight + climateWeight
        if (breakInDays < 1)
            breakInDays = 1
        return breakInDays
    }
}