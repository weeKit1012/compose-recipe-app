package com.example.composerecipeapp.domain.use_case.recipe

import com.example.composerecipeapp.domain.model.Recipe
import com.example.composerecipeapp.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetRecipesForType(
        private val repository: RecipeRepository
)
{
    operator fun invoke(type: String): Flow<List<Recipe>>
    {
        return repository.getRecipesForType(type)
    }
}