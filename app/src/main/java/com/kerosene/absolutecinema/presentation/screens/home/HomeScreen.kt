package com.kerosene.absolutecinema.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kerosene.absolutecinema.R
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.getApplicationComponent
import com.kerosene.absolutecinema.presentation.extensions.getMoviesForSelectedTab

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val component = getApplicationComponent()
    val viewModel: HomeViewModel = viewModel(factory = component.getViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onSearchClick = onSearchClick,
        onTabSelected = viewModel::onTabSelected,
        onMovieClick = onMovieClick,
        onLoadDataClick = viewModel::loadMovies,
        modifier = modifier
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUiState,
    onSearchClick: () -> Unit,
    onTabSelected: (HomeScreenUiState.Tab) -> Unit,
    onMovieClick: (Int) -> Unit,
    onLoadDataClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        SearchButton(onClick = onSearchClick)

        when (uiState) {
            is HomeScreenUiState.Initial -> InitialState(onLoadDataClick = onLoadDataClick)
            is HomeScreenUiState.Loading -> LoadingState()
            is HomeScreenUiState.Error -> ErrorState(message = uiState.message)
            is HomeScreenUiState.Content -> {
                HomeTabs(
                    uiState = uiState,
                    onTabSelected = onTabSelected
                )
                MoviesContent(
                    movies = uiState.getMoviesForSelectedTab(),
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
private fun SearchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.search_home))
        }
    }
}

@Composable
private fun HomeTabs(
    uiState: HomeScreenUiState,
    onTabSelected: (HomeScreenUiState.Tab) -> Unit,
) {
    val selectedTabIndex = when (uiState) {
        is HomeScreenUiState.Content -> uiState.selectedTab.ordinal
        else -> 0
    }

    TabRow(selectedTabIndex = selectedTabIndex) {
        HomeScreenUiState.Tab.entries.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(tab) },
                text = { Text(tab.displayName) }
            )
        }
    }
}

@Composable
private fun InitialState(onLoadDataClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.click_load_movies),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLoadDataClick) {
                Text(text = stringResource(R.string.load))
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Red
        )
    }
}

@Composable
private fun MoviesContent(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
) {
    if (movies.isEmpty()) {
        EmptyMoviesList()
    } else {
        MoviesGrid(
            movies = movies,
            onMovieClick = onMovieClick
        )
    }
}

@Composable
private fun EmptyMoviesList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(R.string.list_movies_empty))
    }
}

@Composable
fun MoviesGrid(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
) {
    if (movies.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.list_movies_empty))
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = movies,
                key = { it.id }
            ) { movie ->
                MoviePreviewCard(movie = movie) {
                    onMovieClick(movie.id)
                }
            }
        }
    }
}
