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

    fun isValidCredentials(username: String, password: String, userId: String, onResult: (Boolean) -> Unit) {
        dbHelper.readableDatabase.use { readableDb ->
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
                var isValidPassword = false
                do {
                    // Iterate over each user found with the given username
                    val storedPassword =
                        cursor.getString(cursor.getColumnIndex(UserAccountDataBaseHelper.COLUMN_PASSWORD))
                    if (password == storedPassword) {
                        // Password matches for one of the users, consider it valid
                        isValidPassword = true
                        break // exit the loop once a match is found
                    }
                } while (cursor.moveToNext())
                isValidPassword
            } else {
                // User does not exist, create the user
                createUser(username, password, userId)
                true // Assume successful creation as this is a simplified example
            }

            cursor.close()

            onResult(isValid)

            if (isValid) {
                appPreferences.userId = userId
                appPreferences.userName = username
            }
        }
    }

    fun clearUserDatabase(onResult: (Boolean) -> Unit) {
        try {
            // Open the writable database
            val writableDb = dbHelper.writableDatabase

            // Specify the table to delete records from
            val table = UserAccountDataBaseHelper.TABLE_NAME

            // Delete all rows
            val rowsDeleted = writableDb.delete(table, null, null)

            // Close the database
            writableDb.close()

            // Notify the result based on the number of rows deleted
            onResult(rowsDeleted > 0)
        } catch (e: Exception) {
            // Handle any exceptions, log or notify as needed
            onResult(false)
        }
    }

    private fun createUser(username: String, password: String, userId: String) {
        dbHelper.writableDatabase.use { writableDb ->
            val values = ContentValues().apply {
                put(UserAccountDataBaseHelper.COLUMN_ID, userId)
                put(UserAccountDataBaseHelper.COLUMN_USERNAME, username)
                put(UserAccountDataBaseHelper.COLUMN_PASSWORD, password)
            }
            writableDb.insert(UserAccountDataBaseHelper.TABLE_NAME, null, values)
        }
    }

}