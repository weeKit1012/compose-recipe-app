package com.example.composerecipeapp.domain.model

import androidx.room.PrimaryKey

data class User(
        val userName: String,
        val userPin: String,
        @PrimaryKey val id: Int? = null
)
