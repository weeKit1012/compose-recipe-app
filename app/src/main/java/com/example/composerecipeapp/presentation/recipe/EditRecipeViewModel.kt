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
import com.example.composerecipeapp.presentation.util.TempRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

sealed interface EditRecipeEvent
{
    data class OnEnterTitle(val str: String) : EditRecipeEvent
    data class OnEnterDescription(val str: String) : EditRecipeEvent
    data class OnEnterIngredients(val str: String) : EditRecipeEvent
    data class OnEnterSteps(val str: String) : EditRecipeEvent
    data class OnSelectCategoryType(val str: String) : EditRecipeEvent
    data class OnSelectImage(val imgUri: Uri?) : EditRecipeEvent
    object OnClickEditRecipe: EditRecipeEvent
}

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
        private val useCases: RecipeUseCases,
        private val uploadImageToStorage: UploadImage,
        @CoroutineIO private val ioScope: CoroutineScope,
        private val tempRepo: TempRepo
) : ViewModel()
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    var objState by mutableStateOf(EditRecipeObjState())
        private set

    var typeListState by mutableStateOf(EditRecipeTypeListState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init
    {
        this.getRecipeTypes()
        this.setupState()
    }

    /* ===================================================== */
    /* Public Methods */
    /* ===================================================== */

    fun onEvent(event: EditRecipeEvent)
    {
        when (event)
        {
            is EditRecipeEvent.OnEnterTitle ->
            {
                objState = objState.copy(
                        title = event.str
                )
            }
            is EditRecipeEvent.OnEnterDescription ->
            {
                objState = objState.copy(
                        description = event.str
                )
            }
            is EditRecipeEvent.OnEnterIngredients ->
            {
                objState = objState.copy(
                        ingredients = event.str
                )
            }
            is EditRecipeEvent.OnEnterSteps ->
            {
                objState = objState.copy(
                        steps = event.str
                )
            }
            is EditRecipeEvent.OnSelectCategoryType ->
            {
                objState = objState.copy(
                        category_type = event.str
                )
                typeListState = typeListState.copy(
                        selectedType = event.str
                )
            }
            is EditRecipeEvent.OnSelectImage ->
            {
                event.imgUri?.let {
                    this.uploadImageToStorage(it)
                }
            }

            is EditRecipeEvent.OnClickEditRecipe ->
            {
                this.editRecipe()
            }
        }
    }

    /* ===================================================== */
    /* Private Methods */
    /* ===================================================== */

    private fun setupState()
    {
        tempRepo.passedRecipe?.let {
            this.objState = objState.copy(
                    title = it.title,
                    description = it.description,
                    ingredients = it.ingredients,
                    steps = it.steps,
                    category_type = it.categoryType,
                    uploadedImageUri = it.imageUri
            )

            this.typeListState = typeListState.copy(
                    selectedType = it.categoryType
            )
        }
    }

    private fun getRecipeTypes()
    {
        viewModelScope.launch {

            val typeList = useCases.getRecipeTypes.invoke()

            typeListState = typeListState.copy(
                    recipeTypeList = typeList
            )
        }
    }

    private fun uploadImageToStorage(_uri: Uri)
    {
        viewModelScope.launch {
            val strStorageUri = uploadImageToStorage.invoke(_uri)
            objState = objState.copy(
                    uploadedImageUri = strStorageUri.toUri()
            )
        }
    }

    private fun editRecipe()
    {
        ioScope.launch {

            if (objState.uploadedImageUri == null)
            {
                _uiEvent.send(UiEvent.showSnackBar("Please wait for a while..."))
                return@launch
            }

            if (objState.title.isEmpty() || objState.description.isEmpty() || objState.ingredients.isEmpty() || objState.steps.isEmpty() || objState.category_type.isEmpty())
            {
                _uiEvent.send(UiEvent.showSnackBar("All fields are mandatory"))
                return@launch
            }

            useCases.updateRecipe(
                    Recipe(
                            title = objState.title,
                            description = objState.description,
                            ingredients = objState.ingredients,
                            steps = objState.steps,
                            categoryType = objState.category_type,
                            imageUri = objState.uploadedImageUri!!,
                            dateCreated = tempRepo.passedRecipe?.dateCreated ?: LocalDate.now(),
                            id = tempRepo.passedRecipe?.id
                    )
            )

            _uiEvent.send(UiEvent.NavigateTo(Route.OVERVIEW_RECIPE))
        }
    }
}

data class EditRecipeObjState(
        val title: String = "",
        val description: String = "",
        val ingredients: String = "",
        val steps: String = "",
        val category_type: String = "",
        val uploadedImageUri: Uri? = null,
)

data class EditRecipeTypeListState(
        val recipeTypeList: List<String> = emptyList(),
        val selectedType: String = ""
)
