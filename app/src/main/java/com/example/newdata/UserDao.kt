package com.example.newdata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table")
    fun getAll(): List<User>

    @Query("SELECT * FROM user_table WHERE roll_num LIKE :roll LIMIT 1")
    suspend fun findByRoll(roll: Int): User

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()


}