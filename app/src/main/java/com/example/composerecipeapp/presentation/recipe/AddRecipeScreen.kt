package com.example.composerecipeapp.presentation.recipe

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composerecipeapp.domain.util.UiEvent
import com.example.composerecipeapp.presentation.util.*

@Composable
fun AddRecipeScreen(
        viewModel: AddRecipeViewModel = hiltViewModel(),
        scaffoldState: ScaffoldState,
        onNavigateUp: () -> Unit,
        onNavigateTo: (String) -> Unit
)
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    val state = viewModel.state
    val focusManager = LocalFocusManager.current

    /* ===================================================== */
    /* Collector */
    /* ===================================================== */

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it)
            {
                is UiEvent.NavigateTo ->
                {
                    onNavigateTo(it.route)
                }

                is UiEvent.showSnackBar ->
                {
                    scaffoldState.snackbarHostState.showSnackbar(it.msg, duration = SnackbarDuration.Short)
                }
            }
        }
    }

    /* ===================================================== */
    /* UI */
    /* ===================================================== */

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = 20.dp)
        .verticalScroll(rememberScrollState(), enabled = true)
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        RANavigationBar(strTitle = "Add Recipe",
                onNavigateBackOnClicked = onNavigateUp
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)) {

            // Image
            RAImagePicker(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
                    onSelectImageUri = {
                        viewModel.onEvent(AddRecipeEvent.OnSelectImageUri(imageUri = it))
                    },
                    currentImageUri = state.imageUri,
                    isClickToSelectEnabled = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            RATextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        viewModel.onEvent(AddRecipeEvent.OnEnterTitle(strTitle = it))
                    }, strLabel = "Title",
                    strPlaceHolder = "",
                    value = state.title)

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            RATextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        viewModel.onEvent(AddRecipeEvent.OnEnterDescription(strDescription = it))
                    }, strLabel = "Description",
                    strPlaceHolder = "",
                    value = state.description)

            Spacer(modifier = Modifier.height(10.dp))

            // Ingredients
            RATextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        viewModel.onEvent(AddRecipeEvent.OnEnterIngredients(strIngredients = it))
                    }, strLabel = "Ingredients",
                    strPlaceHolder = "",
                    value = state.ingredients)

            Spacer(modifier = Modifier.height(10.dp))

            // Steps
            RATextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        viewModel.onEvent(AddRecipeEvent.OnEnterSteps(strSteps = it))
                    }, strLabel = "Steps",
                    strPlaceHolder = "",
                    value = state.steps)

            // Recipe Type
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(modifier = Modifier.align(CenterVertically), text = "Recipe Type", style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.primaryVariant)

                RASpinner(items = state.recipeTypeList,
                        selectedItem = state.selectedRecipeType,
                        onItemSelected = {
                            viewModel.onEvent(AddRecipeEvent.OnSelectRecipeType(it))
                        },
                        selectedItemFactory = { modifier, item ->
                            Row(
                                    modifier = modifier
                                        .padding(8.dp)
                                        .wrapContentSize()
                            ) {
                                Text(item)

                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        dropdownItemFactory = { item, _ ->
                            Text(text = item)
                        }
                )
            }


            Spacer(modifier = Modifier.height(30.dp))

            RAActionButton(
                    text = "Confirm",
                    onClick = { viewModel.onEvent(AddRecipeEvent.OnClickAddRecipe) }
            )
        }

    }
}