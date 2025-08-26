package com.kerosene.absolutecinema.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    homeScreenContent: @Composable () -> Unit,
    searchScreenContent: @Composable () -> Unit,
    libraryScreenContent: @Composable () -> Unit,
    movieDetailsScreenContent: @Composable (String) -> Unit,
    editNoteScreenContent: @Composable (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeGraph.route
    ) {
        homeScreenNavGraph(
            homeScreenContent = homeScreenContent,
            movieDetailsScreenContent = movieDetailsScreenContent
        )
        searchScreenNavGraph(
            searchScreenContent = searchScreenContent,
            movieDetailsScreenContent = movieDetailsScreenContent
        )
        libraryScreenNavGraph(
            libraryScreenContent = libraryScreenContent,
            movieDetailsScreenContent = movieDetailsScreenContent,
            editNoteScreenContent = editNoteScreenContent
        )
    }
}