package com.example.composerecipeapp.domain.repository

import com.example.composerecipeapp.domain.model.User

interface UserRepository
{
    suspend fun registerUser(user: User)

    fun validateUser(userName: String, userPin: String): User?
}