package com.example.composerecipeapp.domain.use_case.user

import com.example.composerecipeapp.domain.repository.UserRepository
import com.example.composerecipeapp.domain.util.LoginResult

class LoginUser(
        private val repository: UserRepository
)
{
    operator fun invoke(userName:String, userPin: String): LoginResult
    {
        repository.validateUser(userName, userPin)?.let {
            return LoginResult.Success
        }

        return LoginResult.Failed("User not found")
    }
}