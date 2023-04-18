package com.example.composerecipeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composerecipeapp.domain.model.Recipe
import com.example.composerecipeapp.presentation.launch.LaunchScreen
import com.example.composerecipeapp.presentation.navigation.Route
import com.example.composerecipeapp.presentation.recipe.AddRecipeScreen
import com.example.composerecipeapp.presentation.recipe.EditRecipeScreen
import com.example.composerecipeapp.presentation.recipe.RecipeDetailScreen
import com.example.composerecipeapp.presentation.recipe.RecipeOverviewScreen
import com.example.composerecipeapp.ui.theme.ComposeRecipeAppTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRecipeAppTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(modifier = Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState
                ) {
                    NavHost(
                            navController = navController,
                            startDestination = Route.LAUNCH
                    ) {
                        composable(Route.LAUNCH) {
                            LaunchScreen(
                                    scaffoldState = scaffoldState,
                                    onNavigateTo = {
                                        navController.navigate(it)
                                    }
                            )
                        }
                        composable(Route.OVERVIEW_RECIPE) {

                            BackHandler(true) {
                                // Do Nothing
                            }

                            RecipeOverviewScreen(
                                    scaffoldState = scaffoldState,
                                    onNavigateTo = { route, id ->

                                        when (route)
                                        {
                                            Route.RECIPE_DETAIL ->
                                            {
                                                navController.navigate("$route/$id")
                                            }

                                            else ->
                                            {
                                                navController.navigate(route)
                                            }
                                        }
                                    }
                            )
                        }
                        composable(Route.ADD_RECIPE) {
                            AddRecipeScreen(
                                    scaffoldState = scaffoldState,
                                    onNavigateUp = {
                                        navController.navigateUp()
                                    },
                                    onNavigateTo = {
                                        navController.navigate(it)
                                    }
                            )
                        }

                        composable(Route.UPDATE_RECIPE) {
                            EditRecipeScreen(
                                    scaffoldState = scaffoldState,
                                    onNavigateUp = {
                                        navController.navigateUp()
                                    },
                                    onNavigateTo = {
                                        navController.navigate(it)
                                    }
                            )
                        }

                        composable(route = Route.RECIPE_DETAIL + "/{id}",
                                arguments = listOf(
                                        navArgument("id") {
                                            type = NavType.IntType
                                        }
                                )) {

                            val passedInId = it.arguments?.getInt("id")

                            passedInId?.let {
                                RecipeDetailScreen(selectedId = passedInId,
                                        onNavigateTo = {
                                            navController.navigate(it)
                                        }, onNavigateUp = {
                                    navController.navigateUp()
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}