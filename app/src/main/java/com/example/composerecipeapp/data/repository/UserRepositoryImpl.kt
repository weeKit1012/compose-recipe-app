package com.example.composerecipeapp.data.repository

import com.example.composerecipeapp.data.data_source.dao.UserDAO
import com.example.composerecipeapp.domain.mapper.toUser
import com.example.composerecipeapp.domain.mapper.toUserEntity
import com.example.composerecipeapp.domain.model.User
import com.example.composerecipeapp.domain.repository.UserRepository

class UserRepositoryImpl(
        private val dao: UserDAO
): UserRepository
{
    override suspend fun registerUser(user: User)
    {
        dao.insertUser(user.toUserEntity())
    }

    override fun validateUser(userName: String, userPin: String): User?
    {
        dao.getUser(userName, userPin)?.let {
            return it.toUser()
        }

        return null
    }
}