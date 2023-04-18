package com.example.composerecipeapp.presentation.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import com.example.composerecipeapp.domain.util.UiEvent
import com.example.composerecipeapp.presentation.util.BtnNavRightType
import com.example.composerecipeapp.presentation.util.RAImagePicker
import com.example.composerecipeapp.presentation.util.RANavigationBar
import com.example.composerecipeapp.presentation.util.RATwoLineText

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RecipeDetailScreen(
        viewModel: RecipeDetailViewModel = hiltViewModel(),
        selectedId: Int,
        onNavigateTo: (String) -> Unit,
        onNavigateUp: () -> Unit,
)
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    val state = viewModel.state

    /* ===================================================== */
    /* Collectors */
    /* ===================================================== */

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it)
            {
                is UiEvent.NavigateTo ->
                {
                    onNavigateTo(it.route)
                }
            }
        }
    }

    /* ===================================================== */
    /* UI */
    /* ===================================================== */

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState(), enabled = true)
        .padding(bottom = 20.dp)
    ) {
        RANavigationBar(strTitle = "Recipe Detail",
                onNavigateBackOnClicked = onNavigateUp,
                onNavRightOnClicked = {
                    viewModel.onEvent(RecipeDetailEvent.ProceedToEdit)
                }, btnNavRightType = BtnNavRightType.EDIT)

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

            RAImagePicker(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
                    onSelectImageUri = {
                        // Do Nothing
                    },
                    currentImageUri = state.recipe?.imageUri
            )
            Spacer(modifier = Modifier.height(30.dp))
            state.recipe?.title?.let { RATwoLineText(strTopText = "Recipe Title", strBottomText = it) }
            Spacer(modifier = Modifier.height(20.dp))
            state.recipe?.description?.let { RATwoLineText(strTopText = "Recipe Description", strBottomText = it) }
            Spacer(modifier = Modifier.height(20.dp))
            state.recipe?.ingredients?.let { RATwoLineText(strTopText = "Recipe Ingredients", strBottomText = it) }
            Spacer(modifier = Modifier.height(20.dp))
            state.recipe?.steps?.let { RATwoLineText(strTopText = "Recipe Steps", strBottomText = it) }
            Spacer(modifier = Modifier.height(20.dp))
            state.recipe?.categoryType?.let { RATwoLineText(strTopText = "Recipe Type", strBottomText = it) }
            Spacer(modifier = Modifier.height(20.dp))
            state.recipe?.dateCreated?.let { RATwoLineText(strTopText = "Date Created", strBottomText = it.toString()) }
        }
    }
}