package com.kerosene.absolutecinema.presentation.screens.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kerosene.absolutecinema.R
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.getApplicationComponent

@Composable
fun LibraryScreen(
    onMovieClick: (Int) -> Unit,
    onNoteClick: (Note) -> Unit,
) {
    val component = getApplicationComponent()
    val viewModel: LibraryViewModel = viewModel(factory = component.getViewModelFactory())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LibraryScreenContent(
        uiState = uiState,
        onTabSelected = viewModel::onTabSelected,
        onMovieClick = onMovieClick,
        onNoteClick = onNoteClick,
        onToggleFavourite = { movie -> viewModel.toggleFavourite(movie) },
        modifier = Modifier
    )
}

@Composable
private fun LibraryScreenContent(
    uiState: LibraryScreenUiState,
    onTabSelected: (LibraryScreenUiState.Content.Tab) -> Unit,
    onMovieClick: (Int) -> Unit,
    onNoteClick: (Note) -> Unit,
    onToggleFavourite: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is LibraryScreenUiState.Loading -> LoadingState()
            is LibraryScreenUiState.Error -> ErrorState(message = uiState.message)
            is LibraryScreenUiState.Content -> {
                LibraryTabs(
                    selectedTab = uiState.selectedTab,
                    onTabSelected = onTabSelected
                )
                when (uiState.selectedTab) {
                    LibraryScreenUiState.Content.Tab.FAVOURITES -> FavouritesContent(
                        movies = uiState.favouriteMovies,
                        onMovieClick = onMovieClick,
                        onToggleFavourite = onToggleFavourite
                    )
                    LibraryScreenUiState.Content.Tab.NOTES -> NotesContent(
                        notes = uiState.notes,
                        onNoteClick = onNoteClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryTabs(
    selectedTab: LibraryScreenUiState.Content.Tab,
    onTabSelected: (LibraryScreenUiState.Content.Tab) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier
    ) {
        LibraryScreenUiState.Content.Tab.entries.forEach { tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                text = { Text(text = tab.displayName) }
            )
        }
    }
}

@Composable
private fun FavouritesContent(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onToggleFavourite: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    if (movies.isEmpty()) {
        EmptyState(message = stringResource(R.string.favourite_movies_empty))
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(movies) { movie ->
                FavouriteMovieItem(
                    movie = movie,
                    onMovieClick = onMovieClick,
                    onToggleFavourite = { onToggleFavourite(movie) }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FavouriteMovieItem(
    movie: Movie,
    onMovieClick: (Int) -> Unit,
    onToggleFavourite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
            .clickable { onMovieClick(movie.id) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GlideImage(
                model = movie.poster?.url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = onToggleFavourite,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
        )
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
            color = MaterialTheme.colorScheme.error
        )
    }
}