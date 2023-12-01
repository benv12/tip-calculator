package com.example.newdata.home

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newdata.common.AppPreferences
import com.example.newdata.database.GuestListDataBaseHelper
import com.example.newdata.model.GuestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val _appPreferences: AppPreferences,
    private val context: Context
) : ViewModel() {
    val userName: String? = _appPreferences.userName
    val userId: String? = _appPreferences.userId
    val isUserAdded: Boolean? = _appPreferences.isUserAdded

    // Use the same variable name for consistency
    private val dbHelper = GuestListDataBaseHelper(context)

    private val _guestList = MutableLiveData<List<GuestModel>>()
    private val _guestCount = MutableLiveData<Int>()

    val guestList: LiveData<List<GuestModel>> = _guestList
    val guestCount: LiveData<Int> = _guestCount

    init {
        // Load guest list and count from the database when ViewModel is created
        loadGuestListFromDatabase()
        loadGuestCountFromDatabase()
    }

    fun loadGuestsFromDatabase() {
        loadGuestListFromDatabase()
    }

    fun setUserAdded(boolean: Boolean) {
        _appPreferences.isUserAdded = boolean
    }

    private fun loadGuestListFromDatabase() {
        val cursor: Cursor = dbHelper.readableDatabase.query(
            GuestListDataBaseHelper.TABLE_NAME,
            arrayOf(GuestListDataBaseHelper.COLUMN_GUEST_NAME),
            null,
            null,
            null,
            null,
            null
        )

        val guestList = mutableListOf<GuestModel>()
        with(cursor) {
            while (moveToNext()) {
                val guestName =
                    getString(getColumnIndexOrThrow(GuestListDataBaseHelper.COLUMN_GUEST_NAME))
                guestList.add(GuestModel(guestName))
            }
        }

        _guestList.value = guestList

        // Close the cursor after use
        cursor.close()
    }

    private fun loadGuestCountFromDatabase() {
        dbHelper.readableDatabase.use { db ->
            val countQuery = "SELECT COUNT(*) FROM ${GuestListDataBaseHelper.TABLE_NAME}"
            val cursor = db.rawQuery(countQuery, null)

            cursor.moveToFirst()
            val count = cursor.getInt(0)
            _guestCount.value = count

            cursor.close()
        }
    }

    private fun addGuestAndUpdateDatabase(guestName: String) {
        // Add guest to the in-memory list
        val updatedList = _guestList.value?.toMutableList() ?: mutableListOf()
        updatedList.add(GuestModel(guestName))
        _guestList.value = updatedList

        // Add guest to the database
        dbHelper.writableDatabase.use { db ->
            val values = ContentValues().apply {
                put(GuestListDataBaseHelper.COLUMN_GUEST_NAME, guestName)
            }

            // Insert the data into the table
            db.insert(GuestListDataBaseHelper.TABLE_NAME, null, values)
        }

        // Update guest count
        _guestCount.value = (_guestCount.value ?: 0) + 1
    }

    private fun removeGuestAndUpdateDatabase(guest: GuestModel) {
        // Remove guest from the in-memory list
        val updatedList = _guestList.value?.toMutableList() ?: mutableListOf()
        updatedList.remove(guest)
        _guestList.value = updatedList

        // Remove guest from the database
        dbHelper.writableDatabase.use { db ->
            val selection = "${GuestListDataBaseHelper.COLUMN_GUEST_NAME} = ?"
            val selectionArgs = arrayOf(guest.guestName)

            // Delete the data from the table
            db.delete(GuestListDataBaseHelper.TABLE_NAME, selection, selectionArgs)
        }

        // Update guest count
        _guestCount.value = (_guestCount.value ?: 0) - 1
    }

    fun invalidateUserAndDataBase() {
        _appPreferences.userId = null
        _appPreferences.userName = null
        dbHelper.writableDatabase.use { db ->
            db.delete(GuestListDataBaseHelper.TABLE_NAME, null, null)
        }
    }

    fun addGuest(guestName: String) {
        val updatedList = _guestList.value?.toMutableList() ?: mutableListOf()
        updatedList.add(GuestModel(guestName))
        addGuestAndUpdateDatabase(guestName)
        _guestList.value = updatedList
    }

    fun removeGuest(guest: GuestModel) {
        val updatedList = _guestList.value?.toMutableList() ?: mutableListOf()
        updatedList.remove(guest)
        removeGuestAndUpdateDatabase(guest)
        _guestList.value = updatedList
    }

}