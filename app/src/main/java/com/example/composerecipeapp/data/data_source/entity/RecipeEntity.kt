package com.example.composerecipeapp.data.data_source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
        val title: String,
        val description: String,
        val ingredients: String,
        val steps: String,
        val categoryType: String,
        val imageFilePath: String,
        val dayOfDate: Int,
        val monthOfDate: Int,
        val yearOfDate: Int,
        @PrimaryKey val id: Int? = null
)