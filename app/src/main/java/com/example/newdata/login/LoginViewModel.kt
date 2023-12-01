package com.example.newdata.login

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.ViewModel
import com.example.newdata.common.AppPreferences
import com.example.newdata.database.UserAccountDataBaseHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val context: Context
) : ViewModel() {

    private val dbHelper = UserAccountDataBaseHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    fun isValidCredentials(username: String, password: String, userId: String, onResult: (Boolean) -> Unit) {
        val readableDb = dbHelper.readableDatabase

        val projection = arrayOf(
            UserAccountDataBaseHelper.COLUMN_ID,
            UserAccountDataBaseHelper.COLUMN_USERNAME,
            UserAccountDataBaseHelper.COLUMN_PASSWORD
        )

        val selection = "${UserAccountDataBaseHelper.COLUMN_USERNAME} = ?"
        val selectionArgs = arrayOf(username)

        val cursor = readableDb.query(
            UserAccountDataBaseHelper.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val isValid = if (cursor.moveToFirst()) {
            // User exists, check password
            val storedPassword =
                cursor.getString(cursor.getColumnIndex(UserAccountDataBaseHelper.COLUMN_PASSWORD))
            password == storedPassword
        } else {
            // User does not exist, create the user
            createUser(username, password, userId)
            true // Assume successful creation as this is a simplified example
        }

        cursor.close()
        readableDb.close()

        onResult(isValid)

        if (isValid) {
            appPreferences.userId = userId
            appPreferences.userName = username
        }
    }

    private fun createUser(username: String, password: String, userId: String) {
        val values = ContentValues().apply {
            put(UserAccountDataBaseHelper.COLUMN_ID, userId)
            put(UserAccountDataBaseHelper.COLUMN_USERNAME, username)
            put(UserAccountDataBaseHelper.COLUMN_PASSWORD, password)
        }
        db.insert(UserAccountDataBaseHelper.TABLE_NAME, null, values)
    }

    // Other functions...
}