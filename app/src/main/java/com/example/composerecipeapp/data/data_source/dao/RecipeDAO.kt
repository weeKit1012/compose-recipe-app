package com.example.composerecipeapp.data.data_source.dao

import androidx.room.*
import com.example.composerecipeapp.data.data_source.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipeentity")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipeentity WHERE id = :id LIMIT 1")
    fun getRecipeForId(id: Int): RecipeEntity?

    @Query("SELECT * FROM recipeentity WHERE categoryType = :type")
    fun getRecipesForType(type: String): Flow<List<RecipeEntity>>
}