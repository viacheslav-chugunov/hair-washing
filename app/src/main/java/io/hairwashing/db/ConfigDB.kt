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
        private const val TABLE_NAME = ConfigDBHelper.DB_TABLE_NAME
        private const val KEY_ID = ConfigDBHelper.DB_KEY_ID
        private const val KEY_VALUE = ConfigDBHelper.DB_KEY_VALUE
        private const val KEY_ARGUMENT = ConfigDBHelper.DB_KEY_ARGUMENT

        const val VALUE_HAIR_TYPE = ConfigDBHelper.DB_VALUE_HAIR_TYPE
        const val VALUE_HAIR_LENGTH = ConfigDBHelper.DB_VALUE_HAIR_LENGTH
        const val VALUE_LAST_WASHING = ConfigDBHelper.DB_VALUE_LAST_WASHING
        const val VALUE_TIME_RANGE = ConfigDBHelper.DB_VALUE_TIME_RANGE
    }

    fun close() { db.close() }

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
        updateLastWashingBy(hair.lastWashing.toString())
        updateTimeRangeBy(timeRange.view)
    }

    fun updateHairTypeBy(arg: String) = updateBy(VALUE_HAIR_TYPE, arg)
    fun updateHairLengthBy(arg: String) = updateBy(VALUE_HAIR_LENGTH, arg)
    fun updateLastWashingBy(arg: String) = updateBy(VALUE_LAST_WASHING, arg)
    fun updateTimeRangeBy(arg: String) = updateBy(VALUE_TIME_RANGE, arg)

    private fun updateBy(value: String, arg: String) {
        val content = ContentValues().apply {
            put(KEY_ARGUMENT, arg)
        }
        db.update(TABLE_NAME, content, "$KEY_VALUE = ?", arrayOf(value))
    }
}