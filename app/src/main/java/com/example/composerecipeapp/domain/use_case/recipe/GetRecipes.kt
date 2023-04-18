package com.example.composerecipeapp.domain.use_case.recipe

import com.example.composerecipeapp.domain.model.Recipe
import com.example.composerecipeapp.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetRecipes(
        private val repository: RecipeRepository
)
{
    operator fun invoke(): Flow<List<Recipe>>
    {
        return repository.getRecipes()
    }
}

class GetRecipeForId(
        private val repository: RecipeRepository
)
{
    operator fun invoke(id: Int): Recipe
    {
        return repository.getRecipeForId(id)!!
    }
}