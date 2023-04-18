package com.example.composerecipeapp.domain.model

import android.net.Uri
import androidx.room.PrimaryKey
import java.time.LocalDate

data class Recipe(
        val title: String,
        val description: String,
        val ingredients: String,
        val steps: String,
        var categoryType: String,
        val imageUri: Uri,
        val dateCreated: LocalDate,
        @PrimaryKey val id: Int? = null
)
