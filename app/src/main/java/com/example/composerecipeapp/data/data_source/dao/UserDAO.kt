package com.example.composerecipeapp.data.data_source.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composerecipeapp.data.data_source.entity.UserEntity

@Dao
interface UserDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM userentity WHERE userName = :userName AND userPin = :userPin LIMIT 1")
    fun getUser(userName: String, userPin: String): UserEntity?
}