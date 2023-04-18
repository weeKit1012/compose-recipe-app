package com.example.composerecipeapp.presentation.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composerecipeapp.di.CoroutineIO
import com.example.composerecipeapp.domain.model.Recipe
import com.example.composerecipeapp.domain.use_case.recipe.RecipeUseCases
import com.example.composerecipeapp.domain.util.UiEvent
import com.example.composerecipeapp.presentation.navigation.Route
import com.example.composerecipeapp.presentation.util.TempRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RecipeOverviewEvent
{
    data class OnSelectRecipe(val selectedRecipe: Recipe) : RecipeOverviewEvent
    data class OnDeleteRecipe(val selectedRecipe: Recipe?) : RecipeOverviewEvent
    data class OnSelectRecipeType(val selectedType: String) : RecipeOverviewEvent
    object OnClickAddRecipe : RecipeOverviewEvent
    object OnClickApplyFilter : RecipeOverviewEvent
    object OnClickCancelFilter : RecipeOverviewEvent
    data class OnTriggerDialog(val isShowing: Boolean, val selectedRecipe: Recipe? = null) : RecipeOverviewEvent
}

@HiltViewModel
class RecipeOverviewViewModel @Inject constructor(
        private val useCases: RecipeUseCases,
        @CoroutineIO private val ioScope: CoroutineScope,
        private val tempRepo: TempRepo
) : ViewModel()
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    var state by mutableStateOf(RecipeOverviewState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init
    {
        this.getRecipeTypes()
        this.refreshList()
    }

    /* ===================================================== */
    /* Public Methods */
    /* ===================================================== */

    fun onEvent(event: RecipeOverviewEvent)
    {
        when (event)
        {
            is RecipeOverviewEvent.OnSelectRecipe ->
            {
                ioScope.launch {
                    tempRepo.passedRecipe = event.selectedRecipe
                    _uiEvent.send(UiEvent.NavigateTo(Route.RECIPE_DETAIL, passedId = event.selectedRecipe.id))
                }
            }

            is RecipeOverviewEvent.OnDeleteRecipe ->
            {
                event.selectedRecipe?.let {
                    this.deleteRecipe(it)
                }
            }

            is RecipeOverviewEvent.OnClickAddRecipe ->
            {
                ioScope.launch {
                    _uiEvent.send(UiEvent.NavigateTo(Route.ADD_RECIPE))
                }
            }

            is RecipeOverviewEvent.OnTriggerDialog ->
            {
                this.state = state.copy(
                        tempSelectedRecipe = event.selectedRecipe,
                        showDeleteDialog = event.isShowing
                )
            }

            is RecipeOverviewEvent.OnSelectRecipeType ->
            {
                this.state = state.copy(
                        tempSelectedRecipeType = event.selectedType
                )
            }

            is RecipeOverviewEvent.OnClickApplyFilter ->
            {
                if (state.tempSelectedRecipeType.isEmpty())
                {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.showSnackBar("Please at least select a type"))
                    }
                    return
                }

                this.getRecipeForType()
            }

            is RecipeOverviewEvent.OnClickCancelFilter ->
            {
                this.state = state.copy(
                        tempSelectedRecipeType = ""
                )

                this.refreshList()
            }
        }
    }

    /* ===================================================== */
    /* Private Methods */
    /* ===================================================== */

    private fun refreshList()
    {
        viewModelScope.launch {
            useCases.getRecipes.invoke().collect {

                state = state.copy(
                        recipeList = it
                )
            }
        }
    }

    private fun deleteRecipe(recipe: Recipe)
    {
        ioScope.launch {
            useCases.deleteRecipe(recipe)

            // Reset
            state = state.copy(
                    tempSelectedRecipe = null
            )
        }
    }

    private fun getRecipeTypes()
    {
        viewModelScope.launch {

            val typeList = useCases.getRecipeTypes.invoke()

            state = state.copy(
                    recipeTypeList = typeList
            )
        }
    }

    private fun getRecipeForType()
    {
        viewModelScope.launch {
            useCases.getRecipeForType(state.tempSelectedRecipeType).collect {
                state = state.copy(
                        recipeList = it
                )
            }
        }
    }
}

data class RecipeOverviewState(
        val recipeList: List<Recipe> = emptyList(),
        val recipeTypeList: List<String> = emptyList(),
        val tempSelectedRecipeType: String = "",
        val tempSelectedRecipe: Recipe? = null,
        val showDeleteDialog: Boolean = false
)