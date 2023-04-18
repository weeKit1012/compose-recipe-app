package com.example.composerecipeapp.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.example.composerecipeapp.R
import com.example.composerecipeapp.data.data_source.dao.RecipeDAO
import com.example.composerecipeapp.domain.mapper.toRecipe
import com.example.composerecipeapp.domain.mapper.toRecipeEntity
import com.example.composerecipeapp.domain.model.Recipe
import com.example.composerecipeapp.domain.repository.RecipeRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.xmlpull.v1.XmlPullParser
import java.time.LocalDate

class RecipeRepositoryImpl(
        private val dao: RecipeDAO,
        private val context: Context
) : RecipeRepository
{
    override suspend fun insertRecipe(recipe: Recipe)
    {
        dao.insertRecipe(recipe.toRecipeEntity())
    }

    override suspend fun updateRecipe(recipe: Recipe)
    {
        dao.updateRecipe(recipe.toRecipeEntity())
    }

    override suspend fun deleteRecipe(recipe: Recipe)
    {
        dao.deleteRecipe(recipe.toRecipeEntity())
    }

    @OptIn(FlowPreview::class)
    override fun getRecipes(): Flow<List<Recipe>>
    {
        val sampleDataListFlow = flow {
            emit(getRecipeSampleData())
        }

        return dao.getRecipes().map { entities ->
            entities.map {
                it.toRecipe()
            }
        }.flatMapConcat { list1 ->
            sampleDataListFlow.map { list2 ->
                list1 + list2
            }
        }
    }

    override fun getRecipeForId(id: Int): Recipe?
    {
        return dao.getRecipeForId(id)?.toRecipe()
    }

    override fun getRecipeTypes(): List<String>
    {
        val returnedList = mutableListOf<String>()

        val parser = context.resources.getXml(R.xml.recipetypes)
        while (parser.eventType != XmlPullParser.END_DOCUMENT)
        {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "item")
            {
                val item = parser.nextText()
                returnedList.add(item)
            }
            parser.next()
        }

        return returnedList
    }

    @OptIn(FlowPreview::class)
    override fun getRecipesForType(type: String): Flow<List<Recipe>>
    {
        val sampleDataListFlow = flow {
            emit(getRecipeSampleData(type = type))
        }

        return dao.getRecipesForType(type = type).map { entities ->
            entities.map {
                it.toRecipe()
            }
        }.flatMapConcat { list1 ->
            sampleDataListFlow.map { list2 ->
                list1 + list2
            }
        }
    }

    private fun getRecipeSampleData(type: String = ""): List<Recipe>
    {
        var sampleList: MutableList<Recipe> = mutableListOf()

        val parser = context.resources.getXml(R.xml.recipesampledata)

        while (parser.eventType != XmlPullParser.END_DOCUMENT)
        {
            if (parser.eventType == XmlPullParser.START_TAG)
            {
                if (parser.name == "recipe")
                {
                    var _title: String = ""
                    var _description: String = ""
                    var _ingredients: String = ""
                    var _steps: String = ""
                    var _categoryType: String = ""
                    var _imageUri: String = ""
                    var _dateCreated: String = ""
                    var _id: String = ""

                    while (parser.next() != XmlPullParser.END_TAG || parser.name != "recipe")
                    {
                        if (parser.eventType == XmlPullParser.START_TAG)
                        {
                            when (parser.name)
                            {
                                "title" -> _title = parser.nextText()
                                "description" -> _description = parser.nextText()
                                "ingredients" -> _ingredients = parser.nextText()
                                "steps" -> _steps = parser.nextText()
                                "category_type" -> _categoryType = parser.nextText()
                                "image_uri" -> _imageUri = parser.nextText()
                                "date_created" -> _dateCreated = parser.nextText()
                                "id" -> _id = parser.nextText()
                            }
                        }
                    }

                    sampleList.add(
                            Recipe(
                                    title = _title,
                                    description = _description,
                                    ingredients = _ingredients,
                                    steps = _steps,
                                    categoryType = _categoryType,
                                    dateCreated = LocalDate.now(),
                                    imageUri = _imageUri.toUri(),
                                    id = _id.toInt()
                            ))
                }
            }
            parser.next()
        }

        if (type.isNotEmpty())
        {
            sampleList = sampleList.filter { it.categoryType == type } as MutableList<Recipe>
        }

        return sampleList
    }
}