package com.kerosene.absolutecinema.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kerosene.absolutecinema.presentation.navigation.AppNavGraph
import com.kerosene.absolutecinema.presentation.navigation.NavigationItem
import com.kerosene.absolutecinema.presentation.navigation.Screen
import com.kerosene.absolutecinema.presentation.navigation.rememberNavigationState
import com.kerosene.absolutecinema.presentation.screens.details.MovieDetailsScreen
import com.kerosene.absolutecinema.presentation.screens.home.HomeScreen
import com.kerosene.absolutecinema.presentation.screens.library.favourites.LibraryScreen
import com.kerosene.absolutecinema.presentation.screens.library.notes.EditNoteScreen
import com.kerosene.absolutecinema.presentation.screens.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Search,
        NavigationItem.Library
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navigationState.navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    val itemRoute = item.screen.route
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == itemRoute
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = { navigationState.navigateToTab(itemRoute, selected) },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(text = stringResource(id = item.titleResId)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavGraph(
                navController = navigationState.navController,
                homeScreenContent = {
                    HomeScreen(
                        onSearchClick = {
                            navigationState.navigateTo(Screen.SearchGraph.route)
                        },
                        onMovieClick = { movieId ->
                            navigationState.navigateToMovieDetails(movieId, Screen.HomeGraph.route)
                        },
                    )
                },
                searchScreenContent = {
                    SearchScreen(
                        onMovieClick = { movieId ->
                            navigationState.navigateToMovieDetails(
                                movieId,
                                Screen.SearchGraph.route
                            )
                        },
                        onBackClick = {
                            navigationState.navigateTo(Screen.HomeGraph.route)
                        },
                    )
                },
                libraryScreenContent = {
                    LibraryScreen(
                        onMovieClick = { movieId ->
                            navigationState.navigateToMovieDetails(
                                movieId,
                                Screen.LibraryGraph.route
                            )
                        },
                        onNoteClick = { noteId ->
                            navigationState.navigateToEditNote(noteId)
                        },
                    )
                },
                movieDetailsScreenContent = { movieIdString ->
                    val movieId = movieIdString.toInt()
                    MovieDetailsScreen(
                        movieId = movieId,
                    )
                },
                editNoteScreenContent = { movieIdString ->
                    val movieId = movieIdString.toInt()
                    EditNoteScreen(
                        movieId = movieId,
                        onBack = { navigationState.navController.popBackStack() },
                    )
                }
            )
        }
    }
}

