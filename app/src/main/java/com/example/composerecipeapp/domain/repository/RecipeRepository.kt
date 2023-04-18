package com.example.composerecipeapp.domain.repository

import com.example.composerecipeapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository
{
    suspend fun insertRecipe(recipe: Recipe)

    suspend fun updateRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    fun getRecipes(): Flow<List<Recipe>>

    fun getRecipeForId(id: Int): Recipe?

    fun getRecipeTypes(): List<String>

    fun getRecipesForType(type: String): Flow<List<Recipe>>
}