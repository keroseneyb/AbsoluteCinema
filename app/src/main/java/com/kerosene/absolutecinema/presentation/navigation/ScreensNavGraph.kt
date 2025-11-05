package com.kerosene.absolutecinema.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument

private fun enterTransitionHorizontal() = slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn()
private fun exitTransitionHorizontal() = slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut()
private fun popEnterTransitionHorizontal() = slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn()
private fun popExitTransitionHorizontal() = slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut()

fun NavGraphBuilder.homeScreenNavGraph(
    homeScreenContent: @Composable () -> Unit,
    movieDetailsScreenContent: @Composable (String) -> Unit,
) {
    navigation(
        route = Screen.HomeGraph.route,
        startDestination = Screen.Home.route,
        enterTransition = { enterTransitionHorizontal() },
        exitTransition = { exitTransitionHorizontal() },
        popEnterTransition = { popEnterTransitionHorizontal() },
        popExitTransition = { popExitTransitionHorizontal() }
    ) {
        composable(Screen.Home.route) { homeScreenContent() }
        movieDetailsComposable(movieDetailsScreenContent)
    }
}

fun NavGraphBuilder.searchScreenNavGraph(
    searchScreenContent: @Composable () -> Unit,
    movieDetailsScreenContent: @Composable (String) -> Unit,
) {
    navigation(
        route = Screen.SearchGraph.route,
        startDestination = Screen.Search.route,
        enterTransition = { enterTransitionHorizontal() },
        exitTransition = { exitTransitionHorizontal() },
        popEnterTransition = { popEnterTransitionHorizontal() },
        popExitTransition = { popExitTransitionHorizontal() }
    ) {
        composable(Screen.Search.route) { searchScreenContent() }
        movieDetailsComposable(movieDetailsScreenContent)
    }
}

fun NavGraphBuilder.libraryScreenNavGraph(
    libraryScreenContent: @Composable () -> Unit,
    movieDetailsScreenContent: @Composable (String) -> Unit,
    editNoteScreenContent: @Composable (String) -> Unit,
) {
    navigation(
        route = Screen.LibraryGraph.route,
        startDestination = Screen.Library.route,
        enterTransition = { enterTransitionHorizontal() },
        exitTransition = { exitTransitionHorizontal() },
        popEnterTransition = { popEnterTransitionHorizontal() },
        popExitTransition = { popExitTransitionHorizontal() }
    ) {
        composable(Screen.Library.route) { libraryScreenContent() }
        movieDetailsComposable(movieDetailsScreenContent)
        editNoteComposable(editNoteScreenContent)
    }
}

fun NavGraphBuilder.movieDetailsComposable(
    movieDetailsScreenContent: @Composable (String) -> Unit,
) {
    composable(
        route = Screen.ROUTE_MOVIE_DETAILS,
        arguments = listOf(navArgument(Screen.KEY_MOVIE_ID) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val movieId = backStackEntry.arguments?.getString(Screen.KEY_MOVIE_ID)
            ?: throw RuntimeException("movieId argument is missing")
        movieDetailsScreenContent(movieId)
    }
}

fun NavGraphBuilder.editNoteComposable(
    editNoteScreenContent: @Composable (String) -> Unit,
) {
    composable(
        route = Screen.ROUTE_EDIT_NOTE,
        arguments = listOf(navArgument(Screen.KEY_MOVIE_ID) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val movieId = backStackEntry.arguments?.getString(Screen.KEY_MOVIE_ID)
            ?: throw RuntimeException("movieId argument is missing")
        editNoteScreenContent(movieId)
    }
}