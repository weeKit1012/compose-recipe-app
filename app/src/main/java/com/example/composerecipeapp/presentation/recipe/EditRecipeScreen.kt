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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composerecipeapp.domain.util.UiEvent
import com.example.composerecipeapp.presentation.util.*

@Composable
fun EditRecipeScreen(
        viewModel: EditRecipeViewModel = hiltViewModel(),
        scaffoldState: ScaffoldState,
        onNavigateUp: () -> Unit,
        onNavigateTo: (String) -> Unit
)
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    val objState = viewModel.objState
    val typeListState = viewModel.typeListState
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

        RANavigationBar(strTitle = "Edit Recipe",
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
                        viewModel.onEvent(EditRecipeEvent.OnSelectImage(imgUri = it))
                    },
                    currentImageUri = objState.uploadedImageUri,
                    isClickToSelectEnabled = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            RATextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        viewModel.onEvent(EditRecipeEvent.OnEnterTitle(str = it))
                    }, strLabel = "Title",
                    strPlaceHolder = "",
                    value = objState.title)

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            RATextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        viewModel.onEvent(EditRecipeEvent.OnEnterDescription(str = it))
                    }, strLabel = "Description",
                    strPlaceHolder = "",
                    value = objState.description)

            Spacer(modifier = Modifier.height(10.dp))

            // Ingredients
            RATextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        viewModel.onEvent(EditRecipeEvent.OnEnterIngredients(str = it))
                    }, strLabel = "Ingredients",
                    strPlaceHolder = "",
                    value = objState.ingredients)

            Spacer(modifier = Modifier.height(10.dp))

            // Steps
            RATextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        viewModel.onEvent(EditRecipeEvent.OnEnterSteps(str = it))
                    }, strLabel = "Steps",
                    strPlaceHolder = "",
                    value = objState.steps)

            // Recipe Type
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(modifier = Modifier.align(Alignment.CenterVertically), text = "Recipe Type", style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.primaryVariant)

                RASpinner(items = typeListState.recipeTypeList,
                        selectedItem = typeListState.selectedType,
                        onItemSelected = {
                            viewModel.onEvent(EditRecipeEvent.OnSelectCategoryType(it))
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
                    onClick = { viewModel.onEvent(EditRecipeEvent.OnClickEditRecipe) }
            )
        }

    }
}