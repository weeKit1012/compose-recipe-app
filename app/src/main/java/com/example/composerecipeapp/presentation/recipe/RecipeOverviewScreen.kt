package com.example.composerecipeapp.presentation.recipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composerecipeapp.domain.model.Recipe
import com.example.composerecipeapp.domain.util.UiEvent
import com.example.composerecipeapp.presentation.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecipeOverviewScreen(
        viewModel: RecipeOverviewViewModel = hiltViewModel(),
        scaffoldState: ScaffoldState,
        onNavigateTo: (String, Int?) -> Unit
)
{
    /* ===================================================== */
    /* Stored Properties */
    /* ===================================================== */

    val state = viewModel.state

    /* ===================================================== */
    /* Collector */
    /* ===================================================== */

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when (it)
            {
                is UiEvent.NavigateTo ->
                {
                    onNavigateTo(it.route, it.passedId)
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

    LazyColumn(modifier = Modifier
        .fillMaxSize()
    ) {

        item {
            RANavigationBar(strTitle = "Recipe Overview",
                    onNavRightOnClicked = {
                        viewModel.onEvent(RecipeOverviewEvent.OnClickAddRecipe)
                    },
                    btnNavRightType = BtnNavRightType.ADD
            )
        }

        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Filter By", modifier = Modifier.align(Alignment.CenterVertically))
                RASpinner(items = state.recipeTypeList,
                        selectedItem = state.tempSelectedRecipeType,
                        onItemSelected = {
                            viewModel.onEvent(RecipeOverviewEvent.OnSelectRecipeType(it))
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
        }

        item {
            Row(Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                RARegularButton(strTitle = "Apply",
                        buttonColor = MaterialTheme.colors.primary,
                        onClick = {
                            viewModel.onEvent(RecipeOverviewEvent.OnClickApplyFilter)
                        }
                )
                RARegularButton(strTitle = "Cancel",
                        buttonColor = Color.Yellow,
                        onClick = {
                            viewModel.onEvent(RecipeOverviewEvent.OnClickCancelFilter)
                        }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Divider(startIndent = 0.dp, thickness = 1.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(30.dp))
        }

        items(state.recipeList) {
            RecipeItem(item = it, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .combinedClickable(
                        onClick = {
                            viewModel.onEvent(RecipeOverviewEvent.OnSelectRecipe(it))
                        },
                        onLongClick = {
                            viewModel.onEvent(RecipeOverviewEvent.OnTriggerDialog(true, selectedRecipe = it))
                        }
                )
            )
        }
    }

    // Dialog
    if (state.showDeleteDialog)
    {
        AlertDialog(
                onDismissRequest = {
                    viewModel.onEvent(RecipeOverviewEvent.OnTriggerDialog(false))
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onEvent(RecipeOverviewEvent.OnTriggerDialog(false))
                        viewModel.onEvent(RecipeOverviewEvent.OnDeleteRecipe(state.tempSelectedRecipe))
                    }) {
                        Text(text = "Confirm", style = MaterialTheme.typography.caption)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onEvent(RecipeOverviewEvent.OnTriggerDialog(false)) }) {
                        Text(text = "Cancel", style = MaterialTheme.typography.caption)
                    }
                },
                title = { Text(text = "Delete Recipe") },
                text = { Text(text = "Confirm to delete the selected recipe?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = RoundedCornerShape(5.dp),
                backgroundColor = MaterialTheme.colors.background
        )
    }
}