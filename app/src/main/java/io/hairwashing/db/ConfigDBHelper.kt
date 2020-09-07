package io.hairwashing.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.hairwashing.db.ConfigDB.Companion.KEY_ARGUMENT
import io.hairwashing.db.ConfigDB.Companion.KEY_ID
import io.hairwashing.db.ConfigDB.Companion.KEY_VALUE
import io.hairwashing.db.ConfigDB.Companion.TABLE_NAME
import io.hairwashing.db.ConfigDB.Companion.VALUE_CLIMATE
import io.hairwashing.db.ConfigDB.Companion.VALUE_HAIR_LENGTH
import io.hairwashing.db.ConfigDB.Companion.VALUE_HAIR_TYPE
import io.hairwashing.db.ConfigDB.Companion.VALUE_LAST_WASHING
import io.hairwashing.db.ConfigDB.Companion.VALUE_PRE_LAST_WASHING
import io.hairwashing.db.ConfigDB.Companion.VALUE_PRE_SETUP_ON_START
import io.hairwashing.db.ConfigDB.Companion.VALUE_SETUP_VISIBILITY
import io.hairwashing.db.ConfigDB.Companion.VALUE_TIME_RANGE
import io.hairwashing.structure.dependences.Hair
import io.hairwashing.structure.dependences.TimeRange

class ConfigDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private var db: SQLiteDatabase? = null

    companion object {
        private const val DB_NAME = "configDB"
        private const val DB_VERSION = 4
    }

    override fun onCreate(db: SQLiteDatabase?) {
        this.db = db
        db?.execSQL("create table $TABLE_NAME($KEY_ID integer primary key autoincrement, " +
                "$KEY_VALUE text, " +
                "$KEY_ARGUMENT text);")
        initDBWithDefaultArgs()
    }

    private fun initDBWithDefaultArgs() {
        val hairType = Hair.Type.DRY.view
        insertLine(VALUE_HAIR_TYPE, hairType)
        val hairLength = Hair.Length.SHORT.view
        insertLine(VALUE_HAIR_LENGTH, hairLength)
        val lastWashing = Hair.NEVER_WASHING_DATE.toString()
        insertLine(VALUE_LAST_WASHING, lastWashing)
        val preLastWashing = Hair.NEVER_WASHING_DATE.toString()
        insertLine(VALUE_PRE_LAST_WASHING, preLastWashing)
        val climate = Hair.Climate.FRIGID.view
        insertLine(VALUE_CLIMATE, climate)
        val timeRange = TimeRange.ONE_WEEK.view
        insertLine(VALUE_TIME_RANGE, timeRange)
        insertLine(VALUE_SETUP_VISIBILITY, "true")
        insertLine(VALUE_PRE_SETUP_ON_START, "true")
    }

        private fun insertLine(value: String, argument: String) {
            val content = ContentValues().apply {
                put(KEY_VALUE, value)
                put(KEY_ARGUMENT, argument)
            }
            db?.insert(TABLE_NAME, null, content)
        }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)
    }
}