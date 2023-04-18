package com.example.composerecipeapp.data.data_source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
        val userName: String,
        val userPin: String,
        @PrimaryKey val id: Int? = null
)
