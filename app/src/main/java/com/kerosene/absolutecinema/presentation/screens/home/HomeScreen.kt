package com.kerosene.absolutecinema.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.kerosene.absolutecinema.R
import com.kerosene.absolutecinema.getApplicationComponent
import com.kerosene.absolutecinema.presentation.screens.home.model.MoviePreviewUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val component = getApplicationComponent()
    val viewModel: HomeViewModel = viewModel(factory = component.getViewModelFactory())
    val uiState by viewModel.uiState.collectAsState()

    val popularMovies = viewModel.popularMoviesFlow.collectAsLazyPagingItems()
    val allMovies = viewModel.allMoviesFlow.collectAsLazyPagingItems()

    HomeScreenContent(
        uiState = uiState,
        popularMovies = popularMovies,
        allMovies = allMovies,
        onSearchClick = onSearchClick,
        onTabSelected = viewModel::onTabSelected,
        onMovieClick = onMovieClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUiState,
    popularMovies: LazyPagingItems<MoviePreviewUiModel>,
    allMovies: LazyPagingItems<MoviePreviewUiModel>,
    onSearchClick: () -> Unit,
    onTabSelected: (Tab) -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = { Tab.entries.size }
    )

    Column(modifier = modifier.fillMaxSize()) {
        SearchButton(onClick = onSearchClick)

        when (uiState) {
            is HomeScreenUiState.Loading -> LoadingState()
            is HomeScreenUiState.Error -> ErrorState(uiState.message)
            is HomeScreenUiState.Success -> {
                LaunchedEffect(uiState.selectedTab) {
                    val page = uiState.selectedTab.ordinal
                    if (pagerState.currentPage != page) {
                        pagerState.scrollToPage(page)
                    }
                }
                ShowContent(
                    pagerState = pagerState,
                    selectedTab = uiState.selectedTab,
                    onTabSelected = onTabSelected,
                    onMovieClick = onMovieClick,
                    coroutineScope = coroutineScope,
                    popularMovies = popularMovies,
                    allMovies = allMovies
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowContent(
    pagerState: PagerState,
    selectedTab: Tab,
    onTabSelected: (Tab) -> Unit,
    onMovieClick: (Int) -> Unit,
    coroutineScope: CoroutineScope,
    popularMovies: LazyPagingItems<MoviePreviewUiModel>,
    allMovies: LazyPagingItems<MoviePreviewUiModel>,
) {
    val popularGridState = rememberLazyGridState()
    val allGridState = rememberLazyGridState()

    LaunchedEffect(pagerState.currentPage) {
        val newTab = Tab.entries[pagerState.currentPage]
        if (newTab != selectedTab) onTabSelected(newTab)
    }

    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            Tab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(tab.displayName) }
                )
            }
        }

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (Tab.entries[page]) {
                Tab.POPULAR -> MoviesPagingContent(
                    movies = popularMovies,
                    onMovieClick = onMovieClick,
                    gridState = popularGridState
                )
                Tab.ALL -> MoviesPagingContent(
                    movies = allMovies,
                    onMovieClick = onMovieClick,
                    gridState = allGridState
                )
            }
        }
    }
}

@Composable
private fun MoviesPagingContent(
    movies: LazyPagingItems<MoviePreviewUiModel>,
    onMovieClick: (Int) -> Unit,
    gridState: LazyGridState? = null
) {
    val state = gridState ?: rememberLazyGridState()
    val refreshLoadState = movies.loadState.refresh

    when (refreshLoadState) {
        is LoadState.Loading -> LoadingState()
        is LoadState.Error -> ErrorState(refreshLoadState.error.localizedMessage ?: "Unknown error")
        is LoadState.NotLoading -> {
            if (movies.itemCount == 0) {
                EmptyMoviesList()
            } else {
                MoviesPagingGrid(
                    movies = movies,
                    onMovieClick = onMovieClick,
                    gridState = state
                )
            }
        }
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
private fun MoviesPagingGrid(
    movies: LazyPagingItems<MoviePreviewUiModel>,
    onMovieClick: (Int) -> Unit,
    gridState: LazyGridState,
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = movies.itemCount,
            key = movies.itemKey { it.id }
        ) { index ->
            movies[index]?.let { movie ->
                MoviePreviewCard(movie = movie) {
                    onMovieClick(movie.id)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MoviePreviewCard(
    movie: MoviePreviewUiModel,
    onClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(320.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = { onClick(movie.id) })
    ) {
        GlideImage(
            model = movie.poster,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.placeholder),
            failure = placeholder(R.drawable.error)
        )
        RatingBox(
            rating = movie.rating,
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.TopEnd)
        )
    }
}

@Composable
private fun RatingBox(
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(Color.Black, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.kinopoisk_item),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = String.format("%.1f", rating),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
