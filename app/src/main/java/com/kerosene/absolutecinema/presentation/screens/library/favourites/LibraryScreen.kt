package com.kerosene.absolutecinema.presentation.screens.library.favourites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kerosene.absolutecinema.R
import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.presentation.screens.library.favourites.model.MovieLibraryUiModel
import kotlinx.coroutines.launch

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onMovieClick: (Int) -> Unit,
    onNoteClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    LibraryScreenContent(
        uiState = uiState,
        selectedTab = selectedTab,
        onMovieClick = onMovieClick,
        onNoteClick = onNoteClick,
        onTabSelected = { tab ->
            viewModel.onTabSelected(tab)
        },
        onToggleFavourite = { movieId, title ->
            viewModel.toggleFavourite(movieId, title)
        },
    )
}

@Composable
private fun LibraryScreenContent(
    uiState: LibraryScreenUiState,
    selectedTab: LibraryTab,
    onMovieClick: (Int) -> Unit,
    onNoteClick: (Int) -> Unit,
    onTabSelected: (LibraryTab) -> Unit,
    onToggleFavourite: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is LibraryScreenUiState.Loading -> LoadingState()
        is LibraryScreenUiState.Error -> ErrorState(message = uiState.message)
        is LibraryScreenUiState.Success -> SuccessContent(
            uiState = uiState,
            selectedTab = selectedTab,
            onMovieClick = onMovieClick,
            onNoteClick = onNoteClick,
            onTabSelected = onTabSelected,
            onToggleFavourite = onToggleFavourite,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SuccessContent(
    uiState: LibraryScreenUiState.Success,
    selectedTab: LibraryTab,
    onMovieClick: (Int) -> Unit,
    onNoteClick: (Int) -> Unit,
    onTabSelected: (LibraryTab) -> Unit,
    onToggleFavourite: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = selectedTab.ordinal,
        pageCount = { LibraryTab.entries.size }
    )

    LaunchedEffect(selectedTab) {
        val targetPage = selectedTab.ordinal
        if (pagerState.currentPage != targetPage) {
            pagerState.scrollToPage(targetPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        val newTab = LibraryTab.entries[pagerState.currentPage]
        if (newTab != selectedTab) onTabSelected(newTab)
    }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            LibraryTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(tab.displayName) }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            TabContent(
                selectedTab = LibraryTab.entries[page],
                favouriteMovies = uiState.favouriteMovies,
                notes = uiState.notes,
                onMovieClick = onMovieClick,
                onNoteClick = onNoteClick,
                onToggleFavourite = onToggleFavourite
            )
        }
    }
}

@Composable
private fun TabContent(
    selectedTab: LibraryTab,
    favouriteMovies: List<MovieLibraryUiModel>,
    notes: List<Note>,
    onMovieClick: (Int) -> Unit,
    onNoteClick: (Int) -> Unit,
    onToggleFavourite: (Int, String) -> Unit,
) {
    val contentModifier = Modifier.fillMaxSize()

    when (selectedTab) {
        LibraryTab.FAVOURITES -> FavouritesContent(
            movies = favouriteMovies,
            onMovieClick = onMovieClick,
            onToggleFavourite = onToggleFavourite,
            modifier = contentModifier
        )

        LibraryTab.NOTES -> NotesContent(
            notes = notes,
            onNoteClick = onNoteClick,
            modifier = contentModifier
        )
    }
}

@Composable
private fun FavouritesContent(
    movies: List<MovieLibraryUiModel>,
    onMovieClick: (Int) -> Unit,
    onToggleFavourite: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
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
                    onToggleFavourite = { movie, title -> onToggleFavourite(movie, title) }
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FavouriteMovieItem(
    movie: MovieLibraryUiModel,
    onMovieClick: (Int) -> Unit,
    onToggleFavourite: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
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
                model = movie.poster,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = { onToggleFavourite(movie.id, movie.title) },
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
private fun NotesContent(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (notes.isEmpty()) {
        EmptyState(message = stringResource(R.string.notes_empty))
    } else {
        LazyColumn(modifier = modifier) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onClick = { onNoteClick(note.noteId) }
                )
            }
        }
    }
}

@Composable
private fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content.ifEmpty { stringResource(R.string.msg_no_text) },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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
            color = MaterialTheme.colorScheme.error
        )
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
