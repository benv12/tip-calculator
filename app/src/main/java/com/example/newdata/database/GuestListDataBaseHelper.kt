package com.example.newdata.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GuestListDataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "GuestListDatabase.db"

        // Define the table and columns
        const val TABLE_NAME = "guest_list"
        const val COLUMN_ID = "id"
        const val COLUMN_GUEST_NAME = "guest_name"

        // SQL query to create the guest list table
        const val SQL_CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_GUEST_NAME TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the guest list table
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Upgrade policy (if needed) when the database version changes
    }
}