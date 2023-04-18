package com.example.composerecipeapp.presentation.recipe

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composerecipeapp.di.CoroutineIO
import com.example.composerecipeapp.domain.model.Recipe
import com.example.composerecipeapp.domain.use_case.firebase.UploadImage
import com.example.composerecipeapp.domain.use_case.recipe.RecipeUseCases
import com.example.composerecipeapp.domain.util.UiEvent
import com.example.composerecipeapp.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed interface AddRecipeEvent
{
    object OnClickAddRecipe : AddRecipeEvent
    data class OnSelectImageUri(val imageUri: Uri?) : AddRecipeEvent
    data class OnSelectRecipeType(val type: String) : AddRecipeEvent
    data class OnEnterTitle(val strTitle: String) : AddRecipeEvent
    data class OnEnterDescription(val strDescription: String) : AddRecipeEvent
    data class OnEnterIngredients(val strIngredients: String) : AddRecipeEvent
    data class OnEnterSteps(val strSteps: String) : AddRecipeEvent
}

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
        private val useCases: RecipeUseCases,
        private val uploadImageToStorage: UploadImage,
        @CoroutineIO private val ioScope: CoroutineScope
) : ViewModel()
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    var state by mutableStateOf(AddRecipeState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init
    {
        this.getRecipeTypes()
    }

    /* ===================================================== */
    /* Public Methods */
    /* ===================================================== */

    fun onEvent(event: AddRecipeEvent)
    {
        when (event)
        {
            is AddRecipeEvent.OnClickAddRecipe ->
            {
                this.addRecipe()
            }

            is AddRecipeEvent.OnSelectImageUri ->
            {
                event.imageUri?.let {
                    this.state = state.copy(
                            imageUri = it
                    )
                    this.uploadImageToStorage(it)
                }
            }

            is AddRecipeEvent.OnEnterTitle ->
            {
                this.state = state.copy(
                        title = event.strTitle
                )
            }

            is AddRecipeEvent.OnEnterDescription ->
            {
                this.state = state.copy(
                        description = event.strDescription
                )
            }

            is AddRecipeEvent.OnEnterIngredients ->
            {
                this.state = state.copy(
                        ingredients = event.strIngredients
                )
            }

            is AddRecipeEvent.OnEnterSteps ->
            {
                this.state = state.copy(
                        steps = event.strSteps
                )
            }

            is AddRecipeEvent.OnSelectRecipeType ->
            {
                this.state = state.copy(
                        selectedRecipeType = event.type
                )
            }
        }
    }

    /* ===================================================== */
    /* Private Methods */
    /* ===================================================== */

    private fun addRecipe()
    {
        ioScope.launch {

            if (state.imageUri != null && state.uploadedImageUri == null)
            {
                _uiEvent.send(UiEvent.showSnackBar("Please wait for a while..."))
                return@launch
            }

            if (state.title.isEmpty() || state.description.isEmpty() || state.selectedRecipeType.isEmpty() || state.ingredients.isEmpty() || state.steps.isEmpty())
            {
                _uiEvent.send(UiEvent.showSnackBar("All fields are mandatory"))
                return@launch
            }

            useCases.insertRecipe(
                    Recipe(
                            state.title,
                            state.description,
                            state.ingredients,
                            state.steps,
                            state.selectedRecipeType,
                            imageUri = state.uploadedImageUri!!,
                            dateCreated = LocalDate.now()
                    )
            )
            _uiEvent.send(UiEvent.NavigateTo(Route.OVERVIEW_RECIPE))
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

    private fun uploadImageToStorage(_uri: Uri)
    {
        viewModelScope.launch {
            val strStorageUri = uploadImageToStorage.invoke(_uri)
            state = state.copy(
                    uploadedImageUri = strStorageUri.toUri()
            )
        }
    }
}

data class AddRecipeState(
        val title: String = "",
        val description: String = "",
        val ingredients: String = "",
        val steps: String = "",
        var imageUri: Uri? = null,
        var uploadedImageUri: Uri? = null,
        val recipeTypeList: List<String> = listOf(),
        val selectedRecipeType: String = ""
)