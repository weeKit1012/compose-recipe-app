package com.example.composerecipeapp.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composerecipeapp.data.data_source.dao.RecipeDAO
import com.example.composerecipeapp.data.data_source.dao.UserDAO
import com.example.composerecipeapp.data.data_source.entity.RecipeEntity
import com.example.composerecipeapp.data.data_source.entity.UserEntity

@Database(
        entities = [
            UserEntity::class,
            RecipeEntity::class
        ],
        version = 1
)
abstract class RADatabase : RoomDatabase()
{
    abstract val userDAO: UserDAO
    abstract val recipeDAO: RecipeDAO
}