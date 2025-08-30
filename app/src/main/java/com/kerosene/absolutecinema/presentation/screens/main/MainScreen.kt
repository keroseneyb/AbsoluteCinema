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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kerosene.absolutecinema.presentation.navigation.AppNavGraph
import com.kerosene.absolutecinema.presentation.navigation.NavigationItem
import com.kerosene.absolutecinema.presentation.navigation.Screen
import com.kerosene.absolutecinema.presentation.navigation.rememberNavigationState
import com.kerosene.absolutecinema.presentation.screens.details.MovieDetailsScreen
import com.kerosene.absolutecinema.presentation.screens.home.HomeScreen
import com.kerosene.absolutecinema.presentation.screens.library.LibraryScreen
import com.kerosene.absolutecinema.presentation.screens.note.EditNoteScreen
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
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    val selected = currentRoute == item.screen.route

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navigationState.navigateTo(item.screen.route)
                            }
                        },
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
                            navigationState.navigateTo(Screen.Search.route)
                        },
                        onMovieClick = { movieId ->
                            navigationState.navigateToMovieDetails(movieId)
                        }
                    )
                },
                searchScreenContent = {
                    SearchScreen(
                        onMovieClick = { movie ->
                            navigationState.navigateToMovieDetails(movie.id)
                        },
                        onBackClick = {
                            navigationState.navController.popBackStack()
                        }
                    )
                },
                libraryScreenContent = {
                    LibraryScreen(
                        onMovieClick = { movieId ->
                            navigationState.navigateToMovieDetails(movieId)
                        },
                        onNoteClick = { note ->
                            navigationState.navigateToEditNote(note.movieId)
                        }
                    )
                },
                movieDetailsScreenContent = { movieIdString ->
                    val movieId = movieIdString.toInt()
                    MovieDetailsScreen(movieId = movieId)
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

