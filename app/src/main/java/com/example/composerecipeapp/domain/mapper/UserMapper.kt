package com.example.composerecipeapp.domain.mapper

import com.example.composerecipeapp.data.data_source.entity.UserEntity
import com.example.composerecipeapp.domain.model.User

fun UserEntity.toUser(): User
{
    return User(
            userName = userName,
            userPin = userPin,
            id = id
    )
}

fun User.toUserEntity(): UserEntity
{
    return UserEntity(
            userName = userName,
            userPin = userPin,
            id = id
    )
}