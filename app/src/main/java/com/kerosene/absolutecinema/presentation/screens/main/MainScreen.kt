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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.collectAsLazyPagingItems
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
                    val uiState by viewModel.uiState.collectAsState()

                    val popularMovies = viewModel.popularMoviesFlow.collectAsLazyPagingItems()
                    val allMovies = viewModel.allMoviesFlow.collectAsLazyPagingItems()
                    HomeScreen(
                        onSearchClick = {
                            navigationState.navigateTo(Screen.SearchGraph.route)
                        },
                        onMovieClick = { movieId ->
                            navigationState.navigateToMovieDetails(movieId, Screen.HomeGraph.route)
                        },
                        uiState = uiState,
                        popularMovies = popularMovies,
                        allMovies = allMovies,
                        onTabSelected = viewModel::onTabSelected
                    )
                },
                searchScreenContent = {
                    val viewModel: SearchViewModel = viewModel(factory = component.getViewModelFactory())
                    val uiState by viewModel.uiState.collectAsState()
                    val query by viewModel.query.collectAsState()
                    val focusRequester = remember { FocusRequester() }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    val focusManager = LocalFocusManager.current
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
                        uiState = uiState,
                        query = query,
                        focusRequester = focusRequester,
                        focusManager = focusManager,
                        keyboardController = keyboardController,
                        onQueryChange = viewModel::onQueryChange

                    )
                },
                libraryScreenContent = {
                    val viewModel: LibraryViewModel =viewModel(factory = component.getViewModelFactory())
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

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
                        uiState = uiState,
                        selectedTab = selectedTab,
                        onTabSelected = viewModel::onTabSelected,
                        onToggleFavourite = viewModel::toggleFavourite
                    )
                },
                movieDetailsScreenContent = { movieIdString ->
                    val viewModel: MovieDetailsViewModel = viewModel(factory = component.getViewModelFactory())
                    val uiState by viewModel.uiState.collectAsState()
                    val isFavourite by viewModel.isFavourite.collectAsState()
                    val movieId = movieIdString.toInt()
                    MovieDetailsScreen(
                        movieId = movieId,
                        uiState = uiState,
                        isFavourite = isFavourite,
                        loadMovie = viewModel::loadMovie,
                        onToggleFavourite = viewModel::toggleFavourite,
                        onTrailerClick = viewModel::openTrailer
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

