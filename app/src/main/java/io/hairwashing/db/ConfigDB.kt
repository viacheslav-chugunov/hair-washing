package io.hairwashing.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import io.hairwashing.structure.dependences.TimeRange
import io.hairwashing.structure.dependences.Hair
import java.lang.IllegalArgumentException

class ConfigDB(context: Context) {
    private var db: SQLiteDatabase = ConfigDBHelper(context).writableDatabase

    companion object {
        const val TABLE_NAME = "config"
        const val KEY_ID = "_id"
        const val KEY_VALUE = "value"
        const val KEY_ARGUMENT = "argument"

        const val VALUE_HAIR_TYPE = "hair_type"
        const val VALUE_HAIR_LENGTH = "hair_length"
        const val VALUE_CLIMATE = "climate"
        const val VALUE_LAST_WASHING = "last_washing"
        const val VALUE_PRE_LAST_WASHING = "pre_last_washing"
        const val VALUE_TIME_RANGE = "time_range"
        const val VALUE_SETUP_VISIBILITY = "setup_visibility"
    }

    fun close() = db.close()

    fun getArgBy(value: String) : String {
        val cursor = getQueryBy(KEY_VALUE, KEY_ARGUMENT)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (value == cursor.getString(cursor.getColumnIndex(KEY_VALUE))) {
                    val argument = cursor.getString(cursor.getColumnIndex(KEY_ARGUMENT))
                    cursor.close()
                    return argument
                }
            } while (cursor.moveToNext())
        }
        throw IllegalArgumentException("Config database have no value $value")
    }

    private fun getQueryBy(vararg columns: String) : Cursor? {
        return db.query(TABLE_NAME, columns, null,
            null, null, null, null)
    }

    fun updateBy(hair: Hair, timeRange: TimeRange) {
        updateHairTypeBy(hair.type.view)
        updateHairLengthBy(hair.length.view)
        updateClimateBy(hair.climate.view)
        updateLastWashingBy(hair.lastWashing.toString())
        updateTimeRangeBy(timeRange.view)
    }

    fun updateHairTypeBy(arg: String) = updateBy(VALUE_HAIR_TYPE, arg)

    fun updateHairLengthBy(arg: String) = updateBy(VALUE_HAIR_LENGTH, arg)

    fun updateClimateBy(arg: String) = updateBy(VALUE_CLIMATE, arg)

    fun updateLastWashingBy(arg: String) = updateBy(VALUE_LAST_WASHING, arg)

    fun updateTimeRangeBy(arg: String) = updateBy(VALUE_TIME_RANGE, arg)

    fun updateSetupVisibility(arg: String) = updateBy(VALUE_SETUP_VISIBILITY, arg)

        private fun updateBy(value: String, arg: String) {
            val content = ContentValues().apply {
                put(KEY_ARGUMENT, arg)
            }
            db.update(TABLE_NAME, content, "$KEY_VALUE = ?", arrayOf(value))
        }
}