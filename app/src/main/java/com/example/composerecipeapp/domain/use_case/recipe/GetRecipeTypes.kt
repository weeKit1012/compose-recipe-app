package com.example.composerecipeapp.domain.use_case.recipe

import com.example.composerecipeapp.domain.repository.RecipeRepository

class GetRecipeTypes(
        private val repository: RecipeRepository
)
{
    operator fun invoke(): List<String>
    {
        return repository.getRecipeTypes()
    }
}