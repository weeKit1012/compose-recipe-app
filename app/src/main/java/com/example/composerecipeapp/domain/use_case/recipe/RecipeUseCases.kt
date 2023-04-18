package com.example.composerecipeapp.domain.use_case.recipe

data class RecipeUseCases(
        val insertRecipe: InsertRecipe,
        val updateRecipe: UpdateRecipe,
        val deleteRecipe: DeleteRecipe,
        val getRecipes: GetRecipes,
        val getRecipeForId: GetRecipeForId,
        val getRecipeTypes: GetRecipeTypes,
        val getRecipeForType: GetRecipesForType
)
