package com.example.composerecipeapp.domain.use_case.recipe

import com.example.composerecipeapp.domain.model.Recipe
import com.example.composerecipeapp.domain.repository.RecipeRepository

class DeleteRecipe (
        private val repository: RecipeRepository
)
{
    suspend operator fun invoke(recipe: Recipe)
    {
        repository.deleteRecipe(recipe)
    }
}