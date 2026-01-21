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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kerosene.absolutecinema.getApplicationComponent
import com.kerosene.absolutecinema.presentation.navigation.AppNavGraph
import com.kerosene.absolutecinema.presentation.navigation.NavigationItem
import com.kerosene.absolutecinema.presentation.navigation.Screen
import com.kerosene.absolutecinema.presentation.navigation.rememberNavigationState
import com.kerosene.absolutecinema.presentation.screens.details.MovieDetailsScreen
import com.kerosene.absolutecinema.presentation.screens.details.MovieDetailsViewModel
import com.kerosene.absolutecinema.presentation.screens.home.HomeScreen
import com.kerosene.absolutecinema.presentation.screens.home.HomeViewModel
import com.kerosene.absolutecinema.presentation.screens.library.favourites.LibraryScreen
import com.kerosene.absolutecinema.presentation.screens.library.favourites.LibraryViewModel
import com.kerosene.absolutecinema.presentation.screens.library.notes.EditNoteScreen
import com.kerosene.absolutecinema.presentation.screens.library.notes.NoteViewModel
import com.kerosene.absolutecinema.presentation.screens.search.SearchScreen
import com.kerosene.absolutecinema.presentation.screens.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Search,
        NavigationItem.Library
    )
    val component = getApplicationComponent()
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
                    val viewModel: HomeViewModel = viewModel(factory = component.getViewModelFactory())
                    HomeScreen(
                        viewModel = viewModel,
                        onSearchClick = {
                            navigationState.navigateTo(Screen.SearchGraph.route)
                        },
                        onMovieClick = { movieId ->
                            navigationState.navigateToMovieDetails(movieId, Screen.HomeGraph.route)
                        },
                    )
                },
                searchScreenContent = {
                    val viewModel: SearchViewModel = viewModel(factory = component.getViewModelFactory())
                    SearchScreen(
                        viewModel = viewModel,
                        onMovieClick = { movieId ->
                            navigationState.navigateToMovieDetails(
                                movieId,
                                Screen.SearchGraph.route
                            )
                        },
                        onBackClick = {
                            navigationState.navigateTo(Screen.HomeGraph.route)
                        },
                        onQueryChange = { newQuery -> viewModel.onQueryChange(newQuery) }
                    )
                },
                libraryScreenContent = {
                    val viewModel: LibraryViewModel = viewModel(factory = component.getViewModelFactory())
                    LibraryScreen(
                        viewModel = viewModel,
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
                    val viewModel: MovieDetailsViewModel = viewModel(factory = component.getViewModelFactory())
                    MovieDetailsScreen(
                        viewModel = viewModel,
                        movieId = movieId,
                    )
                },
                editNoteScreenContent = { movieIdString ->
                    val movieId = movieIdString.toInt()
                    val viewModel: NoteViewModel = viewModel(factory = component.getViewModelFactory())
                    EditNoteScreen(
                        viewModel = viewModel,
                        movieId = movieId,
                        onBack = { navigationState.navController.popBackStack() },
                    )
                }
            )
        }
    }
}

