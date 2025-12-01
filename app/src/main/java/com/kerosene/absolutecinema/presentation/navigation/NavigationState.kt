package com.kerosene.absolutecinema.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kerosene.absolutecinema.presentation.navigation.utils.popToStartDestinationOfGraph

class NavigationState(
    val navController: NavHostController,
) {
    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToTab(route: String, isSelected: Boolean) {
        if (isSelected) {
            popToRootOfTab(route)
        } else {
            switchToTab(route)
        }
    }

    private fun popToRootOfTab(route: String) {
        navController.popToStartDestinationOfGraph(route)
    }

    private fun switchToTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToMovieDetails(movieId: Int, fromGraph: String) {
        navController.navigate(Screen.MovieDetails.getRouteWithArgs(fromGraph, movieId.toString()))
    }

    fun navigateToEditNote(movieId: Int) {
        navController.navigate(Screen.EditNote.getRouteWithArgs(movieId.toString()))
    }
}

@Composable
fun rememberNavigationState(
    navController: NavHostController = rememberNavController(),
): NavigationState = remember {
    NavigationState(navController)
}