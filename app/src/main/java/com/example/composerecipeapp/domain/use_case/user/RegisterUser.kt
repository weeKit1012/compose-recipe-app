package com.example.composerecipeapp.domain.use_case.user

import com.example.composerecipeapp.domain.model.User
import com.example.composerecipeapp.domain.repository.UserRepository

class RegisterUser(
        private val repository: UserRepository
)
{
    suspend operator fun invoke(userName: String, userPin:String)
    {
        repository.registerUser(User(userName, userPin))
    }
}