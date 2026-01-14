package com.kerosene.absolutecinema.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.kerosene.absolutecinema.R
import com.kerosene.absolutecinema.presentation.screens.home.model.MoviePreviewUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
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
                val currentIsRefreshing = when (uiState.selectedTab) {
                    Tab.POPULAR -> popularMovies.loadState.refresh is LoadState.Loading
                    Tab.ALL -> allMovies.loadState.refresh is LoadState.Loading
                }

                val currentOnRefresh: () -> Unit = {
                    when (uiState.selectedTab) {
                        Tab.POPULAR -> popularMovies.refresh()
                        Tab.ALL -> allMovies.refresh()
                    }
                }

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
                    allMovies = allMovies,
                    isRefreshing = currentIsRefreshing,
                    onRefresh = currentOnRefresh
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
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    val popularGridState = rememberLazyGridState()
    val allGridState = rememberLazyGridState()

    val currentGridState = when (pagerState.currentPage) {
        0 -> popularGridState
        else -> allGridState
    }

    LaunchedEffect(pagerState.currentPage) {
        val newTab = Tab.entries[pagerState.currentPage]
        if (newTab != selectedTab) onTabSelected(newTab)
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                        gridState = popularGridState,
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh
                    )

                    Tab.ALL -> MoviesPagingContent(
                        movies = allMovies,
                        onMovieClick = onMovieClick,
                        gridState = allGridState,
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh
                    )
                }
            }
        }
        ScrollToTopFab(
            gridState = currentGridState,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
private fun ScrollToTopFab(
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val showScrollToTop by remember(gridState) {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0
        }
    }

    AnimatedVisibility(
        visible = showScrollToTop,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
        modifier = modifier
    ) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    gridState.animateScrollToItem(0)
                }
            },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun MoviesPagingContent(
    movies: LazyPagingItems<MoviePreviewUiModel>,
    onMovieClick: (Int) -> Unit,
    gridState: LazyGridState? = null,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    val state = gridState ?: rememberLazyGridState()
    val refreshLoadState = movies.loadState.refresh

    val isInitialLoad = movies.itemCount == 0 && refreshLoadState is LoadState.Loading

    when (refreshLoadState) {
        is LoadState.Error -> ErrorState(refreshLoadState.error.localizedMessage ?: "Unknown error")
        is LoadState.Loading -> {
            if (isInitialLoad) {
                LoadingState()
            } else {
                MoviesPagingGrid(
                    movies = movies,
                    onMovieClick = onMovieClick,
                    gridState = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh
                )
            }
        }

        is LoadState.NotLoading -> {
            if (movies.itemCount == 0) {
                EmptyMoviesList()
            } else {
                MoviesPagingGrid(
                    movies = movies,
                    onMovieClick = onMovieClick,
                    gridState = state,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviesPagingGrid(
    movies: LazyPagingItems<MoviePreviewUiModel>,
    onMovieClick: (Int) -> Unit,
    gridState: LazyGridState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
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
                key = movies.itemKey { it.id },
                contentType = movies.itemContentType { "MovieItem" }
            ) { index ->
                movies[index]?.let { movie ->
                    MoviePreviewCard(movie = movie) {
                        onMovieClick(movie.id)
                    }
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
