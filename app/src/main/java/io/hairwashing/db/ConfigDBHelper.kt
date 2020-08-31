package io.hairwashing.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConfigDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private var db: SQLiteDatabase? = null

    companion object {
        private const val DB_NAME = "configDB"
        private const val DB_VERSION = 1

        const val DB_TABLE_NAME = "config"
        const val DB_KEY_ID = "_id"
        const val DB_KEY_VALUE = "value"
        const val DB_KEY_ARGUMENT = "argument"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        this.db = db
        db?.execSQL("create table $DB_TABLE_NAME($DB_KEY_ID integer primary key autoincrement, " +
                "$DB_KEY_VALUE text, " +
                "$DB_KEY_ARGUMENT text);")

        if (db != null)
            initDBWithDefaultArgs()
    }

    private fun initDBWithDefaultArgs() {
        insertLine("hair_type", "dry")
        insertLine("hair_length", "short")
        insertLine("last_washing", "never")
        insertLine("time_range","one_week")
    }

    private fun insertLine(value: String, argument: String) {
        val content = ContentValues().apply {
            put(DB_KEY_VALUE, value)
            put(DB_KEY_ARGUMENT, argument)
        }
        db?.insert(DB_TABLE_NAME, null, content)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}