package com.example.composerecipeapp.domain.mapper

import androidx.core.net.toUri
import com.example.composerecipeapp.data.data_source.entity.RecipeEntity
import com.example.composerecipeapp.domain.model.Recipe
import java.time.LocalDate

fun RecipeEntity.toRecipe(): Recipe
{
    return Recipe(
            title = title,
            description = description,
            ingredients = ingredients,
            steps = steps,
            categoryType = categoryType,
            imageUri = imageFilePath.toUri(),
            dateCreated = LocalDate.of(yearOfDate, monthOfDate, dayOfDate),
            id = id
    )
}

fun Recipe.toRecipeEntity(): RecipeEntity
{
    return RecipeEntity(
            title = title,
            description = description,
            ingredients = ingredients,
            steps = steps,
            categoryType = categoryType,
            imageFilePath = imageUri.toString(),
            dayOfDate = dateCreated.dayOfMonth,
            monthOfDate = dateCreated.monthValue,
            yearOfDate = dateCreated.year,
            id = id
    )
}