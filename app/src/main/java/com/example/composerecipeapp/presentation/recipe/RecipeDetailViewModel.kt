package com.example.composerecipeapp.presentation.recipe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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

sealed interface RecipeDetailEvent
{
    object ProceedToEdit: RecipeDetailEvent
}

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
        private val useCases: RecipeUseCases,
        @CoroutineIO private val ioScope: CoroutineScope,
        private val tempRepo: TempRepo
) : ViewModel()
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(RecipeDetailState())
        private set

    init
    {
        this.setupState()
    }

    /* ===================================================== */
    /* Public Methods */
    /* ===================================================== */

    fun onEvent(event: RecipeDetailEvent)
    {
        when (event)
        {
            is RecipeDetailEvent.ProceedToEdit ->
            {
                ioScope.launch {
                    _uiEvent.send(UiEvent.NavigateTo(Route.UPDATE_RECIPE))
                }
            }
        }
    }

    /* ===================================================== */
    /* Private Methods */
    /* ===================================================== */

    private fun setupState()
    {
        tempRepo.passedRecipe?.let {
            state = state.copy(
                    recipe = it
            )
        }
    }
}

data class RecipeDetailState(
        val recipe: Recipe? = null
)
