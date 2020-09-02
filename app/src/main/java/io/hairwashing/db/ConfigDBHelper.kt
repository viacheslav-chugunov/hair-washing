package io.hairwashing.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.hairwashing.structure.dependences.Hair
import io.hairwashing.structure.dependences.TimeRange

class ConfigDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private var db: SQLiteDatabase? = null

    companion object {
        private const val DB_NAME = "configDB"
        private const val DB_VERSION = 1

        const val DB_TABLE_NAME = "config"
        const val DB_KEY_ID = "_id"
        const val DB_KEY_VALUE = "value"
        const val DB_KEY_ARGUMENT = "argument"

        const val DB_VALUE_HAIR_TYPE = "hair_type"
        const val DB_VALUE_HAIR_LENGTH = "hair_length"
        const val DB_VALUE_LAST_WASHING = "last_washing"
        const val DB_VALUE_CLIMATE = "climate"
        const val DB_VALUE_TIME_RANGE = "time_range"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        this.db = db
        db?.execSQL("create table $DB_TABLE_NAME($DB_KEY_ID integer primary key autoincrement, " +
                "$DB_KEY_VALUE text, " +
                "$DB_KEY_ARGUMENT text);")
        initDBWithDefaultArgs()
    }

    private fun initDBWithDefaultArgs() {
        val hairType = Hair.Type.DRY.view
        insertLine(DB_VALUE_HAIR_TYPE, hairType)
        val hairLength = Hair.Length.SHORT.view
        insertLine(DB_VALUE_HAIR_LENGTH, hairLength)
        val lastWashing = Hair.NEVER_WASHING_DATE.toString()
        insertLine(DB_VALUE_LAST_WASHING, lastWashing)
        val climate = Hair.Climate.FRIGID.view
        insertLine(DB_VALUE_CLIMATE, climate)
        val timeRange = TimeRange.ONE_WEEK.view
        insertLine(DB_VALUE_TIME_RANGE, timeRange)
    }

    private fun insertLine(value: String, argument: String) {
        val content = ContentValues().apply {
            put(DB_KEY_VALUE, value)
            put(DB_KEY_ARGUMENT, argument)
        }
        db?.insert(DB_TABLE_NAME, null, content)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $DB_TABLE_NAME")
        onCreate(db)
    }
}